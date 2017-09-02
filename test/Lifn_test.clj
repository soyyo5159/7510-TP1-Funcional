(ns Lifn-test
    (:require 
        [clojure.test :refer :all]
        [Lifn :refer :all]
        [Regla :refer :all]
    )
)

(deftest muchas-llamadas
    (testing "muchas llamadas"
      (let [
          lifn (nuevoLifn 1 2 3)
          f (funcion lifn)
        ]
        (gritar 1 2 )
        (gritar 3 4 )
        (gritar 598 78 )
        (is (= (f) 1))
        (is (not (completa? lifn)))
        (is (= (f) 2))
        (is (not (completa? lifn)))
        (is (= (f) 3))
        (is (completa? lifn))
        (is (= (f) 3))
        (is (= (f) 3))
        (is (= (f) 3))
      )
    )
)

(deftest llamadas-argumentos
    (testing "muchas llamadas y argumentos"
      (let [
          lifn (nuevoLifn 1 2 3)
          f (funcion lifn)
        ]
        (is (= (f 7) 1))
        (is (not (completa? lifn)))
        (is (= (f 32 "a" (list 4 5 6)) 2))
        (is (not (completa? lifn)))
        (is (= (f 4) 3))
        (is (completa? lifn))
        (is (= (f) 3))
        (is (= (f) 3))
        (is (= (f) 3))
      )
    )
)