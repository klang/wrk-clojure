;;http://blip.tv/file/1313398
(defn zipm [keys vals]
	(loop [map {}
				 [k & ks :as keys] keys
				 [v & vs :as vals] vals]
		(if (and keys vals)
			(recur (assoc map k v) ks vs)
			map)))

(zipm [:a :b :c] [1 2 3])

(apply hash-map (interleave [:a :b :c] [1 2 3]))
(into {} (map vector [:a :b :c] [1 2 3]))

;; Functions and Arity Overloading
(defn drop-last
	"Return a lazy seq of all but the last n
  (default 1) items in coll"
	([s] (drop-last 1 s))
	([n s] (map(fn [x _] x) (seq s) (drop n s)))
)

;lookup
'foo -> foo
; namespace lookup
`foo -> user/foo

(def foo 5)
foo -> 5

(resolve 'foo)
(resolve 'list)

(defmacro dummy [x] `(list foo ~x))

(macroexpand '(dummy foo))
->(clojure/list user/foo foo)
; http://blip.tv/file/1313503

;; http://clojure.org/runtime_polymorphism
(defmulti encounter (fn [x y] [(:Species x) (:Species y)]))
(defmethod encounter [:Bunny :Lion] [b l] :run-away)
(defmethod encounter [:Lion :Bunny] [l b] :eat)
(defmethod encounter [:Lion :Lion] [l1 l2] :fight)
(defmethod encounter [:Bunny :Bunny] [b1 b2] :mate)
(def b1 {:Species :Bunny :other :stuff})
(def b2 {:Species :Bunny :other :stuff})
(def l1 {:Species :Lion :other :stuff})
(def l2 {:Species :Lion :other :stuff})

(encounter b1 b2) -> :mate
(encounter b1 l1) -> :run-away
(encounter l1 b1) -> :eat
(encounter l1 l2) -> :fight

;; metadata

(def v [1 2 3])
(def trusted-v (with-meta v {:source :trusted}))

(:source ^trusted-v) -> :trusted
(:source ^v) -> nil

(= v trusted-v) -> true

; concurrency 19:30

;; NOTE: commute .. a way to tell the transaction system, that you don't care about the value, you just want it to be one more, when you finish. That way concurrent processes can operate on the value at the same time and none of them have to redo the operation.

(import '(java.util.concurrent Executors))
(defn test-stm [nitems nthreads niters]
  (let [refs  (map ref (replicate nitems 0))
        pool  (Executors/newFixedThreadPool nthreads)
        tasks (map (fn [t]
                      (fn []
                        (dotimes [n niters]
                          (dosync
                            (doseq [r refs]
                              (alter r + 1 t))))))
                   (range nthreads))]
    (doseq [future (.invokeAll pool tasks)]
      (.get future))
    (.shutdown pool)
    (map deref refs)))

;; Refs in action
;http://blip.tv/file/982823