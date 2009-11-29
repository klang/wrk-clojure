(ns ericlavigne
  (:use compojure.http)
  (:use compojure.html)
  (:require [compojure.http.servlet :as servlet])
  (:require [compojure.http.routes :as routes])
  (:require [compojure.server.jetty :as jetty])
  (:require [clojure.contrib.sql :as sql]))

(defn article-title-to-url-name [title]
  (.replaceAll (.toLowerCase title) "[^a-z0-9]+" "-"))

(defn article-url [article]
  (str
    "/articles/"
    (article-title-to-url-name
      (article :title))))

(defn go-home []
  (redirect-to "/articles/"))

; Database

(def db {:classname    "org.postgresql.Driver"
         :subprotocol  "postgresql"
         :subname      "production"
         :user         "postgres"})

(def db {:classname "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         ;;:subname "c:/klang/wrk-clojure/projects/journal-server/articles.db"
         :subname "/home/klang/wrk-clojure/projects/journal-server/articles.db"})

(defn sql-query [query]
  (sql/with-connection db
    (sql/with-query-results res
      query (into [] res))))

(defn fetch-articles []
  (sql-query "select * from article"))

(defn fetch-article [title]
  (first
    (filter
      (fn [art]
        (=
          (article-title-to-url-name title)
          (article-title-to-url-name (art :title))))
      (fetch-articles))))

; Renderers

(defn render-article [article]
  [:div

    [:p [:em (article :description)]]
    (article :body)])

(defn render-article-link [article]
  (link-to
    (article-url article)
    (article :title)))

; Layout

(defn page [session title body]
  (html
    [:head [:title title]]
    [:body
      [:h1 title]
      body
      (dosync
        (cond
          (not (@session :name))
            [:p (link-to "/login/" "Log in")]
          (= (@session :name) "admin")
            [:div
              [:p (link-to "/admin/" "Admin page")]
              [:p (link-to "/logout/" "Log out admin")]]
          :else
            [:p (link-to "/logout/"
                  (str "Log out " (@session :name)))]))]))

; Views

(defn view-article [session title]
  (try
    (let [article (fetch-article title)]
      (page session (article :title)
        (render-article article)))
    (catch Exception ex
      (go-home))))

(defn view-article-list [session]
  (page session "Articles"
    [:dl (mapcat
           (fn [article]
             (list
               [:dt (render-article-link article)]
               [:dd (article :description)]))
           (fetch-articles))]))

(defn login-view [session]
  (page session "Log in"
    [:form {:method "post"}
      "User name: "
        [:input {:name "name", :type "text"}]
        [:br]
      "Password: "
        [:input {:name "password", :type "password"}]
        [:br]
      [:input {:type "submit" :value "Log in"}]]))

(defn admin-view [session]
  (page session "Admin"
    [:p "Only admin can see this page."]))

; Controllers

(defn login-controller [session params]
  (dosync
    (if
      (and
        (= "secret" (params :password))
        ; Username can include letters, numbers,
        ; spaces, underscores, and hyphens.
        (.matches (params :name) "[\\w\\s\\-]+"))
      (do
        (alter session assoc :name (params :name))
        (go-home))
      (redirect-to "/login/"))))

(defn logout-controller [session]
  (dosync
    (alter session assoc :name nil)
    (go-home)))

(defn ensure-admin-controller [session]
  (dosync
    (if (and (@session :name) (= (@session :name) "admin"))
      :next
      (go-home))))

; Routing

(routes/defroutes journal-servlet
  "URL routing for Eric Lavigne's Journal"
  (ANY "/articles/" (view-article-list session))
  (ANY "/articles/:title"
    (view-article session (params :title)))
  (GET "/login/" (login-view session))
  (POST "/login/" (login-controller session params))
  (ANY "/logout/" (logout-controller session))
  (ANY "/*" (ensure-admin-controller session))
  (ANY "/admin/" (admin-view session))
  (ANY "/*" (go-home)))

; Starting the service

(jetty/defserver journal-server
  {:port 8080}
  "/*" (servlet/servlet journal-servlet))

(jetty/start journal-server)
