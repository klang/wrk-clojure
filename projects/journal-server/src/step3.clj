(ns site)

(load-file "./src/step2.clj")

(use 'compojure.control 'compojure.http.session)

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

;; make sure that there is a /login/, when linking to that route in 'page'
(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/ANY "/*" (http-helpers/redirect-to "/articles/")))

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))

(defn page [session title body]
  (html/html
    [:html
      [:head [:title title]]
      [:body [:h1 title] body
       [:p
	(if (session :login)
	  (html/link-to "/logout/"
			(str "Log out " (session :name)))
	  (html/link-to "/login/" "Log in"))]]]))

;; link to login shows up nicely, login in works, but posted data is not saved

(defn login-controller
  [session params]
  (if (= (params :password) "secret")
    [(session-assoc :login true 
		    :name (params :name))
     (http-helpers/redirect-to "/articles/")]
    [(http-helpers/redirect-to "/login/")]))

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/POST "/login/" (login-controller session params))
  (routes/ANY "/*" (http-helpers/redirect-to "/articles/")))

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))

;; we can now log in .. but we cannot log out.

(defn logout-controller [session]
  [(session-dissoc :login :name)
  (http-helpers/redirect-to "/login/")])

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/POST "/login/" (login-controller session params))
  (routes/ANY "/logout/" (logout-controller session))
  (routes/ANY "/*" (http-helpers/redirect-to "/articles/")))

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))
