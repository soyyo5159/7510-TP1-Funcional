(ns Premisa (:require 
    [Verificador :refer :all]
    [Cumplible :refer :all]
))

(defprotocol Premisa
    (misma-forma? [yo otra])
    (nombre [yo])
    (aridad [yo])
    (argumentos [yo])
    (traducida [yo intercambio])
)

(defn emparejar-variables [p1 p2] 
    {:pre [ (satisfies? Premisa p1) (satisfies? Premisa p2)]}
    (zipmap (argumentos p1) (argumentos p2))
)
(defn- intercambiar-si-posible [mapa elem] (let [res (mapa elem)] (if (nil? res) elem res)) )

(defrecord ^:private RecordPremisa [mi-nombre mis-argumentos] 
    Cumplible
    (cumple? [yo fun] (fun yo))
    Verificador
    (verifica? [yo pregunta repreguntar] (= yo pregunta))
    Premisa
    (misma-forma? [yo otra] 
        (and 
            (= (nombre yo) (nombre otra))
            (= (aridad yo) (aridad otra))
        )
    )
    (nombre [yo] mi-nombre)
    (aridad [yo] (count mis-argumentos))
    (argumentos [yo] mis-argumentos)
    (traducida [yo intercambio] 
        (RecordPremisa. mi-nombre 
            (map 
                (partial intercambiar-si-posible intercambio)
                mis-argumentos
            )
        )
    )
)


(defn premisa 
    [nombre & argumentos] 
    (RecordPremisa. nombre (if (nil? argumentos) (list) argumentos))
)