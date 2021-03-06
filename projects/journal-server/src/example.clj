(use 'compojure)

(defn home-view
  [params session flash]
  (html
   (:xhtml-strict doctype)
   [:html
    [:body
     
     (if (flash :message)
       [:p {:id "flash"} (flash :message)])
     (if (session :login)
       [:p "Hi, " (session :name) ", you are currently logged in! "
        (link-to "/logout" "[Logout]")]
       [:p "Hi, you are not currently logged in! " (link-to "/login" "[Login]")])
     [:div {:class "right"}]]]))

(defn login-view
  [params session]
  (html
   (form-to [:post "/login"]
    (text-field :name)
    (password-field :password)
    (submit-button "Login"))))

(def *password* "secret")
(def *my-port* 8082)


(defn login-controller
  [params session]
  (if (= (params :password) *password*)
    [(session-assoc :login true 
		    :name (params :name))
     (redirect-to "/")]
    [(flash-assoc :message "Incorrect Password.")
     (redirect-to "/")]))
 
(defn logout
  [session]
  [(session-assoc :login false 
		  :name nil)
  (redirect-to "/")])

(defn not-found
  []
  (html
   [:html
    [:body
     [:h2 "Page Not Found!"]]]))

(defroutes blog-routes
  (GET "/" (home-view params session flash))
  (GET "/login" (login-view params session))
  (POST "/login" (login-controller params session))
  (GET "/logout" (logout session))
  (ANY "/*" (not-found)))
 
(decorate blog-routes
          (with-session {:type :memory, :expires 600}))
 
(defserver myserver
  {:port *my-port*} "/*" (servlet blog-routes))

(defn up []
  (start myserver))

(defn down []
  (stop myserver))

;; (up)
;; (down)
