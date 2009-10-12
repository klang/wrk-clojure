;; From: http://groups.google.com/group/clojure/msg/96ed91f823305f02
;; usage:
;; (show Object)   ; give it a class
;; (show Object 1) ; a class and a method number to see details
;; (show {})       ; or give it an instance

(import '(java.io LineNumberReader InputStreamReader PushbackReader)
	'(java.lang.reflect Modifier Method Constructor)
	'(clojure.lang RT))

(use 'clojure.contrib.javadoc)

(defn show
  ([x] (show x nil))
  ([x i]
     (let [c (if (class? x) x (class x))
	   items (sort
		  (for [m (concat (.getFields c)
				  (.getMethods c)
				  (.getConstructors c))]
		    (let [static? (bit-and Modifier/STATIC
					   (.getModifiers m))
			  method? (instance? Method m)
			  ctor?   (instance? Constructor m)
			  text (if ctor?
				 (str "(" (apply str (interpose ", " (.getParameterTypes m))) ")")
				 (str
				  (if (pos? static?) "static ")
				  (.getName m) " : "
				  (if method?
				    (str (.getReturnType m) " ("
					 (count (.getParameterTypes m)) ")")
				    (str (.getType m)))))]
		      [(- static?) method? text (str m) m])))]
       (if i
	 (last (nth items i))
	 (do (println "=== " c " ===")
	     (doseq [[e i] (map list items (iterate inc 0))]
	       (printf "[%2d] %s\n" i (nth e 2))))))))
