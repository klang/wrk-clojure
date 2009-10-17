;; automating tests
(use '[clojure.contrib.seq-utils])

(defn index-filter [pred coll]
  (when pred (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(defn 
  #^{:test (fn []
	     (assert (nil? (index-of-any #{\a} nil)))
	     (assert (nil? (index-of-any #{\a} "")))
	     (assert (nil? (index-of-any nil "foo")))
	     (assert (nil? (index-of-any #{} "foo")))
	     (assert (zero? (index-of-any #{\z \a} "zzabyycdxx")))
	     (assert (= 3 (index-of-any #{\b \y} "zzabyycdxx")))
	     (assert (nil? (index-of-any #{\z} "aba"))))}
  index-of-any 
  [pred coll]
  (first (index-filter pred coll)))

(test #'index-of-any)
(comment
(defn #^{:test (fn []
		 (assert (nil? (busted))))}
  busted [] "busted")
)

(test #'busted)

(use '[clojure.contrib.test-is])

(deftest test-index-of-any-with-nil-args
  (is (nil? (index-of-any #{\a} nil)))
  (is (nil? (index-of-any nil "foo"))))

(deftest test-index-of-any-with-empty-args
  (is (nil? (index-of-any #{\a} "")))
  (is (nil? (index-of-any #{} "foo"))))

(deftest test-index-of-any-with-match
  (is (zero? (index-of-any #{\z \a} "zzabyycdxx")))
  (is (= 3 (index-of-any #{\b \y} "zzabyycdxx"))))

(deftest test-index-of-any-with-without-match
  (is (nil? (index-of-any #{\z} "aba"))))

(run-tests)

(deftest test-that-demonstrates-failure
  (is (= 5 (+ 2 2))))
(deftest test-that-demonstrates-error-message
  (is (= 3 Math/PI) "PI is an integer?"))
(deftest test-divide-by-zero
  (is (thrown? ArithmeticException (/ 5 0))))

(run-tests)
