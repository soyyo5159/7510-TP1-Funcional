(ns Inferencia-test
    (:require [clojure.test :refer :all]
        [Inferencia :refer :all]
      [Regla :refer :all]
      [Premisa :as p]
      [Espia :refer :all]
    )
)

(deftest un-miembro
    (testing "un solo miembro "
      (let [
          inferencia (parsear "cumple(X):-hace(X)")
          espia (nuevoEspia)
          preguntar (fn [x] true)
          preguntarS (envuelta espia preguntar "q")

          ]
          (is (= (nombre inferencia) "cumple"))
          (is (= (argumentos inferencia) (list "X")))
          (is (= (aridad inferencia) 1))
          (is (verifica? inferencia (p/parsear "cumple(jeronimo)") preguntarS))
          (is (= (registro? espia) (list
            "q" (p/parsear "hace(jeronimo)")
            )))
      )
    )
)

(deftest dos-miembros
    (testing "Inferencia con dos miembros "
      (let [
          inferencia (parsear "cumple(X):-hace(X);hizo(X)")
          espia (nuevoEspia)
          preguntar (fn [x] true)
          preguntarS (envuelta espia preguntar "q")

          ]
          (is (= (nombre inferencia) "cumple"))
          (is (= (argumentos inferencia) (list "X")))
          (is (= (aridad inferencia) 1))
          (is (verifica? inferencia (p/parsear "cumple(jeronimo)") preguntarS))
          (is (= (registro? espia) (list
            "q" (p/parsear "hace(jeronimo)")
            "q" (p/parsear "hizo(jeronimo)")
            )))
      )
    )
)

(deftest dos-miembros-mucha-aridad
    (testing "Inferencia con dos miembros "
      (let [
          inferencia (parsear "siempre(X,Y,Z):-aveces(X,Y);aveces(Y,Z)")
          espia (nuevoEspia)
          preguntar (fn [x] true)
          preguntarS (envuelta espia preguntar "q")

          ]
          (is (= (nombre inferencia) "siempre"))
          (is (= (argumentos inferencia) (list "X" "Y" "Z")))
          (is (= (aridad inferencia) 3))
          (is (verifica? inferencia (p/parsear "siempre(sol,playa,verano)") preguntarS))
          (is (= (registro? espia) (list
            "q" (p/parsear "aveces(sol,playa)")
            "q" (p/parsear "aveces(playa,verano)")
            )))
      )
    )
)

(deftest cualquier-cosa
    (testing "Inferencia con dos miembros "
      (let [
          inferencia (parsear "hace bastante (mucho) que no te veo!! cómo estás? (X,Y)")
          ]
          (is (= inferencia nil))
      )
    )
)