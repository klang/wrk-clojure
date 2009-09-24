(ns lang)
(use 'compojure)

(defn last-changed []
  (javascript-tag 
   (html 
    "document.write(\"" 
    [:hr] 
    [:center [:h6 [:i "Last changed on \"+document.lastModified+\"" ]]] 
    "\");")))


(defn statusText []
  (javascript-tag 
   "function statusText(text) {setTimeout('top.window.status=\"'+text+'\";',1);}"))

;; also defined in ./karsten/index.clj, based on ./karsten/index.html
(defn karsten []
  (html
   [:head 
    [:title "Karsten Lang"]
    [:style 
     {:type "text/css"}
     "<!-- A:link {text-decoration: none}A:visited{text-decoration:none}A:active{text-decoration:none}-->" ]]
   [:body {:bgcolor "#000000" :text "#eeeeee" :link "#ffffff" :vlink "#dddddd" :alink "#ffffff"}
    [:p {:align "center"}
     [:img {:src "/karsten/pic/klangsign.small.gif" :width "100" :height "147"}]
     [:img {:src "/karsten/pic/klangintro.small.gif" :width "100" :height "147"}]
     [:img {:src "/karsten/pic/logo.small.gif" :width "100" :height "147"}]
     [:br]
     [:a {:href "http://google.com"} "Karsten Lang"]
     [:br]
     [:br]
     [:a {:href "/karsten/computer/" :onMouseOver "statusText('About my computer')"} "computer"] " - "
     [:a {:href "/karsten/cv/" :onMouseOver "statusText('Curriculum Vitae')"} "curriculum vitae"]
     [:br]
     [:a {:href "/karsten/books/" :onMouseOver "statusText('the books I read')"} "books"] " - "
     [:a {:href "/karsten/movies/" :onMouseOver "statusText('the movies I see')"} "movies"] " - "
     [:a {:href "/karsten/music/" :onMouseOver "statusText('the music I hear')"} "music"] " - "
     [:a {:href "/karsten/life/" :onMouseOver "statusText('life in a So-Called Space Age')"} "life"]
     [:br]
     (statusText)
     [:em 
      "The only true freedom is freedom from the hearts desires" [:br]
      "&amp; the only true happiness....this way lies." [:br]]
     [:a {:href "/karsten/music/dusk.html#quote"
	  :onMouseOver "statusText('The lyrics from TheThe - DUSK')"}
      "Th" [:u "e"] "th" [:u "e"]]
     (last-changed)
     ]]))

(defn hello-world []   
  (html [:head 
	 [:title "Hello World"]]
	[:body [:h1 "Hello there"]]))

(defn centered-message 
  "takes a status and a message and returns html to center the information on the screen"
  [s m]   
  (html 
   [:head 
    [:title (if s s m)]
    [:style 
     {:type "text/css"}
     "<!-- A:link {text-decoration: none}A:visited{text-decoration:none}A:active{text-decoration:none}-->"]]
   [:body {:bgcolor "black" :text "white" :link "white" :vlink "white" :alink "red"}
    [:center [:table {:width "100%" :height "100%" :bgcolor "black"}
	      [:tr {:align "center" :valign "center"}
	       [:td {:align "center" :valign "center"} 
		[:center [:p [:tt "[ " (if s (str s " - ") nil) m " ]"]]]]]]]]))

(defn the-grue []
  (centered-message 
   "404"
   (html "You have been eaten by a " [:a {:href "http://en.wikipedia.org/wiki/Grue_%28monster%29"} "grue"] "..")))

(defn hello-world-centered [] 
  (centered-message nil "Hello World")
  )

(defroutes lang
  (GET "/karsten" (karsten))
  (GET "/karsten/computer/" (centered-message "404" "nothing to see here"))
  (GET "/karsten/*" (the-grue))
  (GET "/hello" (hello-world-centered))
  (ANY "/*" (hello-world)))

(defserver lang-server
   {:port 8080}
   "/*" (servlet lang))

(start lang-server)
;; --> (stop lang-server)
