(ns lang)
(use 'compojure)
(load-file "./karsten/index.clj")

(defn hello-world []   
  (html [:head 
	 [:title "Hello World"]]
	[:body [:h1 "Hello there"]]))

(defn the-grue []   
  (html [:head 
	 [:title "you were eaten by a grue"]]
	[:body [:h1 "you were eaten by a grue"]]))

(defn last-changed []
  (html [:script {:language "JavaScript"} 
	 "document.write(\"<hr><center><h6><i>Last changed on \"+ document.lastModified + \"</i></h6></center>\");"]))

(defn statusText []
    (html [:script {:language "JavaScript"} 
	   "function statusText(text) { setTimeout('top.window.status=\"'+text+'\";',1); }"]
	  )
    )

(defroutes lang
  (GET "/karsten" (karsten))
  (GET "/karsten/computer" (hello-world))
  (GET "/karsten/*" (the-grue))
  (GET "/hello" (hello-world))
  (ANY "/*" "Bad URL"))

(defserver lang-server
   {:port 8080}
   "/*" (servlet lang))

(start lang-server)
;; --> (stop lang-server)
