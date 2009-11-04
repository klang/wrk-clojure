(ns site)
;;(load-file "./src/site.clj")
(load-file "./src/step1.clj")

(defn page [session title body]
  (html/html
    [:head [:title title]]
    [:body [:h1 title] body]))
;; server error will occur as the signature of 'page' has been changed

;; fix it by defining the views 
(defn view-article-list [session]
  (page session "Articles"
	[:dl (mapcat
	      (fn [article]
		(list
                 [:dt (render-article-link
		       article)]
                 [:dd (article
		       :description)]))
	      (fetch-articles))]))

(defn render-article [article]
  [:div 
   [:p [:em (article :description)]] 
   (article :body)])

(defn view-article [session title]
  (try
    (let [article (fetch-article title)]
      (page session (article :title)
        (render-article article)))
    (catch Exception ex
      (http-helpers/redirect-to "/articles/"))))

(routes/defroutes journal-servlet
  "Eric Lavigne's Journal"
  (routes/ANY "/articles/" (view-article-list session))
  (routes/ANY "/articles/:title" (view-article session (params :title)))
  (routes/ANY "/*" (http-helpers/redirect-to "/articles/")))

;; the site looks normal, but is dragging around a session
;; Next, we try to use that session for something.
