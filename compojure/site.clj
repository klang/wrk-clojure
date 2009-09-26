;; example found at:
;; http://ericlavigne.wordpress.com/2009/01/04/compojure-security-authentication-and-authorization/
;; http://preview.compojure.org/docs/api
;; (println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))

;;  user> (all-ns)
;; big list of name spaces, check that you've got Compojure on here!

;;  user> (System/getProperty "java.class.path")
;; big class path list, make sure you have Compojure + all the dependent JARs


(ns site
  (:use compojure.http)
  (:use compojure.http.session)
  (:use compojure.html)
  (:use compojure.control)
  (:require [compojure.server.jetty :as jetty]))

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
        (alter session assoc :name (params :name))
				(html [:pre session])
        (redirect-to "/welcome/")
				)
      (redirect-to "/login/"))))

(defn logout-controller [session]
  (dosync
    (alter session assoc :name nil)
    (redirect-to "/goodbye/")))

; Routing

(defroutes hello
  (ANY "/pong" (html [:h1 "ping"]))
	(GET "/ping" (html [:h1 "pong"]))
	(GET "/welcome/" (html [:h1 "welcome"]))
	(GET "/goodbye/" (html [:h1 "goodbye"]))
  (GET "/login/" (login-view))
  (POST "/login/" (login-controller session params))
  (ANY "/logout/" (logout-controller session))
  (ANY "/*" (redirect-to "/welcome/")))

(decorate hello       
          (with-session {:type :memory, :expires 600}))

; Starting the service

(jetty/defserver site
  {:port 8080}
  "/*" (servlet hello))

(jetty/start site)
;;(jetty/stop site)