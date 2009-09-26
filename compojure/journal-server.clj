;; example found at:
;; http://ericlavigne.wordpress.com/2009/01/04/compojure-security-authentication-and-authorization/
;; (println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))

;;  user> (all-ns)
;; big list of name spaces, check that you've got Compojure on here!

;;  user> (System/getProperty "java.class.path")
;; big class path list, make sure you have Compojure + all the dependent JARs


(ns journal-server
  (:use compojure.http)
  (:use compojure.html)
;	(:require [compojure.http.session :as session])
  (:require [compojure.server.jetty :as jetty])
  (:require [clojure.contrib.sql :as sql]))

(defn article-title-to-url-name [title]
  (.replaceAll (.toLowerCase title) "[^a-z0-9]+" "-"))

(defn article-url [article]
  (str
    "/articles/"
    (article-title-to-url-name
      (article :title))))

; Database

(def db {:classname    "org.postgresql.Driver"

         :subprotocol  "postgresql"
         :subname      "production"
         :user         "postgres"})

(defn sql-query [query]
  (sql/with-connection db
    (sql/with-query-results res
      query (into [] res))))

(defn fetch-articles []
  (sql-query "select * from article"))

;; the original table
;; create table article (
;;      id           serial primary key,
;;      title        text not null unique check (trim(title)  ''),
;;      description  text not null default '',
;;      body         text not null default '',
;;      created      timestamp not null default now(),
;;      updated      timestamp not null default now(),
;;      published    timestamp null default null
;; );
(defstruct article 
	:id 
	:title 
	:description 
	:body 
	:created 
	:updated 
	:published)

;; the data we want to fake
;; insert into article (title, description, body) values
;;      ('Article 1', 'My first article',
;;       '<p>Paragraph 1 in article 1</p><p>Another paragraph</p>'),
;;      ('Article 2', 'My second article',
;;       '<p>This article also has a paragraph</p><p>And another</p>');
(struct-map article
	:title 'Article 1'
	:description 'My first article'
	:body '<p>Paragraph 1 in article 1</p><p>Another paragraph</p>'
	:created (. (new java.util.Date) toString)
	:updated (. (new java.util.Date) toString)
	:published (. (new java.util.Date) toString))

(struct-map article 
	:id '2'
	:title 'Article 2'
	:description 'My second article'
	:body '<p>This article also has a paragraph</p><p>And another</p>'
	:created (. (new java.util.Date) toString)
	:updated (. (new java.util.Date) toString)
	:published (. (new java.util.Date) toString))


)

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
    [:html
      [:head [:title title]]
      [:body
        [:h1 title]
        body
        [:p
          (if (@session :name)
            (link-to "/logout/"
              (str "Log out " (@session :name)))
            (link-to "/login/" "Log in"))]]]))

; Views

(defn view-article [session title]
  (try
    (let [article (fetch-article title)]
      (page session (article :title)
        (render-article article)))
    (catch Exception ex
      (redirect-to "/articles/"))))

(defn view-article-list [session]
  (page session "Articles"
    [:dl (mapcat
           (fn [article]
             (list
               [:dt (render-article-link article)]
               [:dd (article :description)]))
           (fetch-articles))]))

(defn login-view []
  (html
    [:form {:method "post"}
      "User name: "
        [:input {:name "name", :type "text"}]
        [:br]
      "Password: "
        [:input {:name "password", :type "password"}]
        [:br]
      [:input {:type "submit" :value "Log in"}]]))

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
        ;(alter session assoc :name (params :name))
				(html [:pre session])
        ;(redirect-to "/articles/")
				)
      (redirect-to "/login/"))))

(defn logout-controller [session]
  (dosync
    (alter session assoc :name nil)
    (redirect-to "/articles/")))

; Routing

;(defservlet journal-servlet
(defroutes journal-servlet
  "Eric Lavigne's Journal"
;  (ANY "/articles/" (view-article-list session))
  (ANY "/articles/" (html [:h1 "articles"]))
;  (ANY "/articles/:title"
;    (view-article session (route :title)))
  (GET "/login/" (login-view))
	(GET "/ping" (html [:h1 "pong"]))
  (POST "/login/" (login-controller session params))
  (ANY "/logout/" (logout-controller session))
  (ANY "/*" (redirect-to "/articles/")))

(decorate journal-server       
          (with-session {:type :memory, :expires 600}))

; Starting the service

(jetty/defserver journal-server
  {:port 8080}
  "/*" (servlet journal-servlet))

(jetty/start journal-server)
;;(jetty/stop journal-server)
