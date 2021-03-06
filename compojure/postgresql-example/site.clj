; http://ericlavigne.wordpress.com/2008/12/28/using-postgresql-with-compojure/
; Indicates which libraries we need, and the
; shorter names by which they will be called
(ns site
  (:require [compojure.http.servlet :as servlet])
  (:require [compojure.http.routes :as routes])
  (:require [compojure.http.helpers :as http-helpers])
  (:require [compojure.html :as html])
  (:require [compojure.html.page-helpers :as page-helpers])
  (:require [compojure.server.jetty :as jetty])
  (:require [clojure.contrib.sql :as sql]))

; Information about the PostgreSQL database
(def db {:classname    "org.postgresql.Driver"
         :subprotocol  "postgresql"
         :subname      "production"
         :user         "postgres"})

; Performs a database query and returns the results as a list
(defn sql-query [query]
  (sql/with-connection db
    (sql/with-query-results res
      ; query is string that may contain "?"
      ; which are replaced with later elements
      ; in this array
      [query]
      (into [] res))))

; Fetches all articles
(defn articles []
  (sql-query "select * from article"))

; Converts "My Article" to "my-article" for use in URL
(defn article-title-to-url-name [title]
  (.replaceAll
    (.toLowerCase title)
    "[^a-z0-9]+" "-"))

; Converts map of article attributes to a URL
(defn article-url [article]
  (str
    "/articles/"
    (article-title-to-url-name
      (article :title))))

; Fetch an article with the given title
(defn article [title]
  (first
    (filter
      (fn [art]
        (=
          (article-title-to-url-name
            title)
          (article-title-to-url-name
            (art :title))))
      (articles))))

; HTML page for an article
(defn render-article [article]
  (html/html
    [:head [:title (article :title)]]
    [:body
      [:h1 (article :title)]
      [:p [:em (article :description)]]
      (article :body)]))

; Search for article by title, return HTML or redirect
(defn view-article [title]
  (try
    (render-article (article title))
    (catch Exception ex
      (http-helpers/redirect-to "/articles/"))))

; HTML link to an article
(defn render-article-link [article]
  (page-helpers/link-to
    (article-url article)
    (article :title)))

; HTML page that lists all articles
(defn view-article-list []
  (html/html
    [:head [:title "Articles"]]
    [:body
      [:dl (mapcat
             (fn [article]
               (list
                 [:dt (render-article-link
                        article)]
                 [:dd (article
                        :description)]))
             (articles))]]))

; Mapping between URLs and view functions
(defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/"
    (view-article-list))
  (routes/ANY "/articles/:title"
    (view-article (route :title)))
  (routes/ANY "/*"
    (http-helpers/redirect-to "/articles/")))

; Server settings
(jetty/defserver journal-server
  {:port 80}
  "/*" journal-servlet)

; Command to start the server
(jetty/start journal-server)
