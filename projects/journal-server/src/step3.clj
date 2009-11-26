(ns site)

(load-file "./src/step2.clj")

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

;; we are going to end up sending a lot of requests to the main page
(defn go-home []
  (http-helpers/redirect-to "/articles/"))

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/ANY "/*" (go-home)))

(use 'compojure.control 'compojure.http.session)

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))
 
;;;;

(defn login-controller [session params]
  (html/html "hello there " (params :name) 
	     " " (= "secret" (params :password))
	     " " (.matches (params :name) "[\\w\\s\\-]+")
	     ;(dosync (alter session assoc :name (params :name)))
	     [(session-assoc :name (params :name))]
	     " session name set to " (read-session :name)
	     ))

(defn show-name [session]
  (html/html "hello there " (session :name)))


(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/GET "/show/" (show-name session))
  (routes/POST "/login/" (login-controller session params))
  (routes/ANY "/*" (go-home)))

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))
 
;;; at least the session works now, but does not seem to contain anything
;; TODO: the rest


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
      (http-helpers/redirect-to "/login/"))))

(defn logout-controller [session]
  (dosync
    (alter session assoc :name nil)
    (go-home)))

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/POST "/login/" (login-controller session params))
  (routes/ANY "/logout/" (logout-controller session))
  (routes/ANY "/*" (go-home)))
