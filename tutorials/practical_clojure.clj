;; "Practical Clojure"  
;; Lisp vs Clojure 
;; Database Example from 'Practical Common Lisp' Chapter 3 
;;;http://groups.google.com/group/clojure/browse_thread/thread/69b781ffe805a7de/b60673e9d4436ce0

;; steps:
;; 1) install Jline Jar
;; 2) run java -cp jline-0.9.93.jar:clojure.jar jline.ConsoleRunner clojure.lang.Repl src/boot.clj
;; 3) copy-and-paste code into REPL or (load-file "practical_clojure.clj") to load entire file

; Lisp Code 
;;;
; Clojure Code      


;(getf (list :a 1 :b 2 :c 3) :a)  
;;;
({:a 1 :b 2 :c 3} :a)

;(defun make-cd (title artist rating ripped)
;	(list :title title :artist artist :rating rating :ripped ripped)) 
;;;
(defn make-cd [title artist rating ripped]
	{:title title :artist artist :rating rating :ripped ripped})     

;(make-cd "Roses" "Kathy Mattea" 7 t)
;;;
(make-cd "Roses" "Kathy Mattea" 7 :t)

;(defvar *db* nil)
;;;
(def *db* (ref []))   

;(defun add-record (cd) (push cd *db*))
;;;
(defn add-record [cd] 
 	(sync nil
    	(set *db* (conj @*db* cd))))    

;(add-record (make-cd "Roses" "Kathy Mattea" 7 t)) 
;(add-record (make-cd "Fly" "Dixie Chicks" "8" t))
;(add-record (make-cd "Home" "Dixie Chicks" "9" t))
;;;
(add-record (make-cd "Roses" "Kathy Mattea" "7" :t))
(add-record (make-cd "Fly" "Dixie Chicks" "8" :t))
(add-record (make-cd "Home" "Dixie Chicks" "9" :t))  

;*db*
;;;
@*db*

;(defun dump-db ()
;  (dolist (cd *db*)
;    (format t "~{~a:~10t~a~%~}~%" cd)))                    
;;;  
(defn format [str & items]
	(.. System out (format str (to-array items)))) 
 
(defn dump-db []
	(doseq cd @*db*
		(doseq key [:title :artist :rating :ripped]
			(format "%-10s%s%n" 
				(strcat ((memfn getName) key) ":") 
				(cd key)))
			(format "%n" nil))) 

;(dump-db)
;;;  
(dump-db)
  
; (defun prompt-read (prompt)
; 	(format *query-io* "~a: " prompt)
; 	(force-output *query-io*)
; 	(read-line *query-io*))  
;;;
(defn prompt-read [prompt]
	(format "%s: " prompt)
	(strcat (read)))    
                  
; (defun prompt-for-cd ()
; 	(make-cd
; 		(prompt-read "Title")
; 		(prompt-read "Artist")
; 		(prompt-read "Rating")
; 		(prompt-read "Ripped [y/n]"))) 
;;;
(defn y-or-n-p [prompt]
	(if (eql? (prompt-read prompt) "y") :t nil)) 
		
(defn prompt-for-cd []
	(make-cd
		(prompt-read "Title")
		(prompt-read "Artist")
		(prompt-read "Rating")
		(y-or-n-p "Ripped [y/n]"))) 

;(prompt-for-cd)
;;;
;(prompt-for-cd)      
	
; (defun add-cds ()
; 	(loop (add-record (prompt-for-cd))
; 	(if (not (y-or-n-p "Another? [y/n]: ")) (return))))
;;;
(defn add-cds []
	(loop []
		(add-record (prompt-for-cd))
		(if (y-or-n-p "Another? [y/n]: ") (recur) ))) 


;(add-cds)
;;;
;(add-cds)    
	
; (defun save-db (filename)
;		(with-open-file (out filename
; 		:direction :output
; 		:if-exists :supersede)
; 	(with-standard-io-syntax
; 	(print *db* out))))  
;;;  
(import '(java.io BufferedWriter OutputStreamWriter FileOutputStream))  

(defn save-db [f]
	(with-open wtr (new BufferedWriter (new OutputStreamWriter (new
	FileOutputStream f) "US-ASCII"))
		(. clojure.lang.RT (print @*db* wtr))))   

(save-db "db.txt")   

; (defun load-db (filename)
; 	(with-open-file (in filename)
; 	(with-standard-io-syntax
; 	(setf *db* (read in)))))     
;;;
(import '(java.io BufferedReader InputStreamReader FileInputStream))  

(defn load-db [f]
	(with-open rdr (new BufferedReader (new InputStreamReader (new
	FileInputStream f) "US-ASCII"))
		 (sync nil
		     (set *db* (load rdr)))))

(load-db "db.txt") 
(dump-db)        
	 
	
; (defun select-by-artist (artist)
; 	(remove-if-not
; 	#'(lambda (cd) (equal (getf cd :artist) artist))
; 	*db*)) 
;;;
(defn select-by-artist [artist]
	(filter
		(fn [cd] 
			(eql? (cd :artist) artist)) @*db*))

(select-by-artist "Dixie Chicks")    
	
;(defun select (selector-fn)
;	(remove-if-not selector-fn *db*))
;;;  
(defn select [selector-fn]
	(filter selector-fn @*db*))
	
	
;(defun foo (&key a b c) (list a b c))
; 	(foo :c 3 :a 1)  
;;;       
(defn foo [#^IPersistentMap kv] (list (kv :a) (kv :b) (kv :c)))	
	(foo {:c 3 :a 1}) 
	
;(defun make-comparison-expr (field value)
;	`(equal (getf cd ,field) ,value)) 
;;;
(defn make-comparison-expr [field value]
	`(eql? (~'cd ~field) ~value))    
	
; (defun make-comparisons-list (fields)
; 	(loop while fields
; 	collecting (make-comparison-expr (pop fields) (pop fields))))
;;;
(defn make-comparisons-list [fields]
	(map make-comparison-expr (keys fields) (vals fields))) 

; (defmacro where (&rest clauses)
; 	`#'(lambda (cd) (and ,@(make-comparisons-list clauses)))) 
;;;
(defmacro where [clauses]
 `(fn [~'cd] (and ~@(make-comparisons-list clauses))))    
	
;(select (where :artist "Dixie Chicks"))	
;;;
(select (where {:artist "Dixie Chicks"}))


;(macroexpand-1 '(where :title "Give Us a Break" :ripped t))
;;;                                                         
(macroexpand-1 '(where {:title "Give Us a Break" :ripped :t}))
