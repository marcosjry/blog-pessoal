package com.blog.pessoal.acelera.maker.util;

public class EndpointConstants {

        private EndpointConstants() {
                throw new UnsupportedOperationException("Classe utilitária.");
        }

        public static final String POSTAGENS = "/api/postagens";
        public static final String POSTAGENS_ID = "/api/postagens/{id}";
        public static final String POSTAGENS_FILTRO = "/api/postagens/filtro?";

        public static final String POSTAGENS_BY_USER = "/stats/posts-per-user";
        public static final String POSTAGENS_BY_DATE = "/stats/posts-by-date";
        public static final String POSTAGENS_TOTAL_NUMBER = "/stats/total-posts";
        public static final String POSTAGENS_LAST_POSTS = "/stats/last-posts";

        public static final String VALIDATE_USER_TOKEN = "/api/usuarios/auth-token";

        public static final String USUARIOS = "/api/usuarios";
        public static final String USUARIOS_LOGIN = "/api/usuarios/login";
        public static final String USUARIOS_ID = "/api/usuarios/{id}";

        public static final String TEMAS = "/api/temas";
        public static final String TEMAS_ID = "/api/temas/{id}";

}
