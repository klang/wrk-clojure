;; example found at:
;; http://dufflebunk.blogspot.com/2009/06/basic-compojure-calculator.html
;; (println (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))

;;  user> (all-ns)
;; big list of name spaces, check that you've got Compojure on here!

;;  user> (System/getProperty "java.class.path")
;; big class path list, make sure you have Compojure + all the dependent JARs


(use 'compojure)

;; simple calculator example
(def op-mapping
     {"add"      +,
      "subtract" -,
      "multiply" *,
      "divide"   /,
      "wagga"    -})

(defn doop [op ls rs]
  "Do the operation on the two values"
  (if (contains? op-mapping op)
    (str ((get op-mapping op) ls rs))
    "Unrecognized operation"))
;; ---

;; simple login exampel
;; from: http://ericlavigne.wordpress.com/2009/01/04/compojure-security-authentication-and-authorization/
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
        (redirect-to "/articles/"))
      (redirect-to "/login/"))))

(defn logout-controller [session]
  (dosync
    (alter session assoc :name nil)
    (redirect-to "/articles/")))

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

;; ---

;; route definitions
(defroutes calc
	;; simple hello world example
	(GET "/hello" (html [:h1 "hello world"]))
  (GET "/:op/:ls/:rs" 
    (doop (params :op) 
	  (. Float valueOf (params :ls)) 
	  (. Float valueOf (params :rs))))
	;; simple login/logout
	(GET "/login/" (login-view))
	(POST "/login/" (login-controller session params))
	(ANY "/logout/" (logout-controller session))
	(GET "/articles/" (html [:h1 "hello world"]))
	;; everything else results in an error
  (ANY "/*" "Bad URL")
	;; we could also redirect to somewhere
	;;(ANY "/*" (redirect-to "/articles/"))
	)

(run-server {:port 8080}
	    "/*" (servlet calc))
