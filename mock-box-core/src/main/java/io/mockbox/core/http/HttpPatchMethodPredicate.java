/*
 * Copyright (c) 2011-2021 VMware, Inc. or its affiliates, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mockbox.core.http;

import static java.util.Objects.requireNonNull;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import reactor.netty.http.server.HttpServerRequest;
import reactor.util.annotation.Nullable;

final class HttpPatchMethodPredicate
        implements Predicate<HttpServerRequest>, Function<Object, Map<String, String>> {
    /**
     * Creates a {@link Predicate} based on a URI template. This will listen for all Methods.
     *
     * @param uri The string to compile into a URI template and use for matching
     * @return The new {@link HttpPatchMethodPredicate}.
     * @see Predicate
     */
    private static Predicate<HttpServerRequest> http(
            String uri, HttpVersion protocol, HttpMethod method) {
        return new HttpPatchMethodPredicate(uri, protocol, method);
    }

    /**
     * An alias for {@link HttpPatchMethodPredicate#http}.
     *
     * <p>Creates a {@link Predicate} based on a URI template filtering .
     *
     * <p>This will listen for POST Method.
     *
     * @param uri The string to compile into a URI template and use for matching
     * @return The new {@link Predicate}.
     * @see Predicate
     */
    public static Predicate<HttpServerRequest> patch(String uri) {
        return http(uri, null, HttpMethod.PATCH);
    }

    final HttpVersion protocol;
    final HttpMethod method;
    final String uri;
    final UriPathTemplate template;

    private HttpPatchMethodPredicate(
            String uri, @Nullable HttpVersion protocol, HttpMethod method) {
        this.protocol = protocol;
        this.uri = requireNonNull(uri, "uri");
        this.method = requireNonNull(method, "method");
        this.template = new UriPathTemplate(uri);
    }

    @Override
    public Map<String, String> apply(Object key) {
        Map<String, String> headers = template.match(key.toString());
        if (null != headers && !headers.isEmpty()) {
            return headers;
        }
        return null;
    }

    @Override
    public boolean test(HttpServerRequest key) {
        return (protocol == null || protocol.equals(key.version()))
                && method.equals(key.method())
                && template.matches(key.uri());
    }

    /**
     * Represents a URI template. A URI template is a URI-like String that contains variables
     * enclosed by braces (<code>{</code>, <code>}</code>), which can be expanded to produce an
     * actual URI.
     *
     * @author Arjen Poutsma
     * @author Juergen Hoeller
     * @author Jon Brisbin
     * @see <a href="https://tools.ietf.org/html/rfc6570">RFC 6570: URI Templates</a>
     */
    private static final class UriPathTemplate {

        private static final Pattern FULL_SPLAT_PATTERN = Pattern.compile("[\\*][\\*]");
        private static final String FULL_SPLAT_REPLACEMENT = ".*";

        private static final Pattern NAME_SPLAT_PATTERN =
                Pattern.compile("\\{([^/]+?)\\}[\\*][\\*]");

        private static final Pattern NAME_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
        // JDK 6 doesn't support named capture groups

        private static final Pattern URL_PATTERN =
                Pattern.compile(
                        "(?:(\\w+)://)?((?:\\[.+?])|(?<!\\[)(?:[^/?]+?))(?::(\\d{2,5}))?([/?].*)?");

        private final List<String> pathVariables = new ArrayList<>();

        private final Pattern uriPattern;

        private static String getNameSplatReplacement(String name) {
            return "(?<" + name + ">.*)";
        }

        private static String getNameReplacement(String name) {
            return "(?<" + name + ">[^\\/]*)";
        }

        static String filterQueryParams(String uri) {
            int hasQuery = uri.lastIndexOf('?');
            if (hasQuery != -1) {
                return uri.substring(0, hasQuery);
            } else {
                return uri;
            }
        }

        static String filterHostAndPort(String uri) {
            if (uri.startsWith("/")) {
                return uri;
            } else {
                Matcher matcher = URL_PATTERN.matcher(uri);
                if (matcher.matches()) {
                    String path = matcher.group(4);
                    return path == null ? "/" : path;
                } else {
                    throw new IllegalArgumentException("Unable to parse url [" + uri + "]");
                }
            }
        }

        /**
         * Creates a new {@code UriPathTemplate} from the given {@code uriPattern}.
         *
         * @param uriPattern The pattern to be used by the template
         */
        UriPathTemplate(String uriPattern) {
            String s = "^" + filterQueryParams(filterHostAndPort(uriPattern));

            Matcher m = NAME_SPLAT_PATTERN.matcher(s);
            while (m.find()) {
                for (int i = 1; i <= m.groupCount(); i++) {
                    String name = m.group(i);
                    pathVariables.add(name);
                    s = m.replaceFirst(getNameSplatReplacement(name));
                    m.reset(s);
                }
            }

            m = NAME_PATTERN.matcher(s);
            while (m.find()) {
                for (int i = 1; i <= m.groupCount(); i++) {
                    String name = m.group(i);
                    pathVariables.add(name);
                    s = m.replaceFirst(getNameReplacement(name));
                    m.reset(s);
                }
            }

            m = FULL_SPLAT_PATTERN.matcher(s);
            while (m.find()) {
                s = m.replaceAll(FULL_SPLAT_REPLACEMENT);
                m.reset(s);
            }

            this.uriPattern = Pattern.compile(s + "$");
        }

        /**
         * Tests the given {@code uri} against this template, returning {@code true} if the uri
         * matches the template, {@code false} otherwise.
         *
         * @param uri The uri to match
         * @return {@code true} if there's a match, {@code false} otherwise
         */
        public boolean matches(String uri) {
            return matcher(uri).matches();
        }

        /**
         * Matches the template against the given {@code uri} returning a map of path parameters
         * extracted from the uri, keyed by the names in the template. If the uri does not match, or
         * there are no path parameters, an empty map is returned.
         *
         * @param uri The uri to match
         * @return the path parameters from the uri. Never {@code null}.
         */
        Map<String, String> match(String uri) {
            Map<String, String> pathParameters = new HashMap<>(pathVariables.size());

            Matcher m = matcher(uri);
            if (m.matches()) {
                int i = 1;
                for (String name : pathVariables) {
                    String val = m.group(i++);
                    pathParameters.put(name, val);
                }
            }
            return pathParameters;
        }

        private Matcher matcher(String uri) {
            uri = filterQueryParams(filterHostAndPort(uri));
            return uriPattern.matcher(uri);
        }
    }
}
