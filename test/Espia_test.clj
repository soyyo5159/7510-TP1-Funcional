(ns Espia-test
    (:require 
        [clojure.test :refer :all]
        [Espia :refer :all]
    )
)

(deftest espionaje-simple
    (testing "un espionaje sencillo"
      (let [
            siempre (fn siempre [] true)
            espia (nuevoEspia) 
            espiada (envuelta espia siempre "siempre")
        ]
            (espiada)
            (espiada)
            (espiada)
            (is (= (registro? espia) (list 
                "siempre"
                "siempre"
                "siempre")) )
        )
    )
)

(deftest espionaje-otra-cosa
    (testing "un espionaje sencillo en el que no se usan nombres sino keywords"
      (let [
            siempre (fn siempre [] true)
            espia (nuevoEspia) 
            espiada (envuelta espia siempre :siempre)
        ]
            (espiada)
            (espiada)
            (espiada)
            (is (= (registro? espia) (list 
                :siempre
                :siempre
                :siempre)) )
        )
    )
)

(deftest espionaje-con-argumentos
    (testing "un espionaje con argumentos"
      (let [
            siempre (fn siempre [x y] true)
            espia (nuevoEspia) 
            espiada (envuelta espia siempre "siempre")
        ]
            (is (espiada 1 2))
            (is (espiada 3 4))
            (is (espiada 5 6))
            (is (= (registro? espia) (list 
                "siempre" 1 2
                "siempre" 3 4
                "siempre" 5 6
            )))
        )
    )
)

(deftest espionaje-con-argumentos-variables
    (testing "un espionaje con argumentos de cantidad variable"
      (let [
            suma (fn suma [& args] (reduce + 0 args))
            espia (nuevoEspia) 
            espiada (envuelta espia suma "suma")
        ]
            (is (= (espiada 1 2) 3))
            (is (= (espiada 1 2 3) 6))
            (is (= (espiada 1) 1))
            (is (= (registro? espia) (list 
                "suma" 1 2
                "suma" 1 2 3
                "suma" 1
            )))
        )
    )
)

(deftest espionaje-con-diccionario
    (testing "un espionaje con diccionario"
      (let [
            dict {"a" 1, "b" 2, "c" 3}
            espia (nuevoEspia) 
            espiada (envuelta espia dict "dict")
        ]
            (is (= (espiada "a") 1))
            (is (= (espiada "b") 2))
            (is (= (espiada "c") 3))
            (is (= (registro? espia) (list 
                "dict" "a"
                "dict" "b"
                "dict" "c"
            )))
        )
    )
)

(deftest espionaje-multiple
    (testing "un espionaje con diccionario"
      (let [
            f {"a" 1, "b" 2, "c" 3}
            g (fn [x] (* 2 x))
            espia (nuevoEspia) 
            f-esp (envuelta espia f "f")
            g-esp (envuelta espia g "g")
        ]
            (f-esp "a")
            (g-esp 1)
            (g-esp 2)
            (g-esp 3)
            (f-esp "c")
            (is (= (registro? espia) (list 
                "f" "a"
                "g" 1
                "g" 2
                "g" 3
                "f" "c"
            )))
        )
    )
)

