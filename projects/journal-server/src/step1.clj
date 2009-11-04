(ns site)

;; starting from the namespace, defined in site.clj, we gradually change
;; things while they are running.
;; each form can be activated indidually or by loading the whole file.

;; first, we wrap the view part in something that handles layout
(defn page [title body]
  (html/html
    [:head [:title title]]
    [:body [:h1 title] body]))

;; the original view-article-list is changed to use the new page layout
(defn view-article-list []
  (page "Articles"
	[:dl (mapcat
	      (fn [article]
		(list
                 [:dt (render-article-link
		       article)]
                 [:dd (article
		       :description)]))
	      (fetch-articles))]))
;; observe, that the site is still online and things look normal

;; the next two forms have to be defined in rapid succession
;; otherwise the users of the site will notice a slight jump
;; in the way things are look
(defn render-article [article]
  [:div 
   [:p [:em (article :description)]] 
   (article :body)])

(defn view-article [title]
  (try
    (let [article (fetch-article title)]
      (page (article :title)
        (render-article article)))
    (catch Exception ex
      (http-helpers/redirect-to "/articles/"))))

;; the site looks normal, but is running with the new layout
;; Next, we add sessions.
