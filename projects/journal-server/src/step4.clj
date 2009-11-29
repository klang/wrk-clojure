(ns site)

(load-file "./src/step3.clj")

(defn go-home []
  (http-helpers/redirect-to "/articles/"))

;; admin 

(defn admin-view [session]
  (page session "Admin"
    [:p "Only admin can see this page."]))

(defn ensure-admin-controller [session]
  (if (and (session :name) (= (session :name) "admin"))
      :next
      (go-home)))

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/GET "/login/" (login-view session))
  (routes/POST "/login/" (login-controller session params))
  (routes/ANY "/logout/" (logout-controller session))
  (routes/ANY "/*" (ensure-admin-controller session))
  (routes/ANY "/admin/" (admin-view session))
  (routes/ANY "/*" (go-home)))

(decorate journal-servlet
          (with-session {:type :memory, :expires 600}))

(defn page [session title body]
  (html/html
    [:head [:title title]]
    [:body
      [:h1 title]
      body
      (cond
          (not (session :name))
            [:p (html/link-to "/login/" "Log in")]
          (= (session :name) "admin")
            [:div
              [:p (html/link-to "/admin/" "Admin page")]
              [:p (html/link-to "/logout/" "Log out admin")]]
          :else
            [:p (html/link-to "/logout/"
                  (str "Log out " (session :name)))])]))

