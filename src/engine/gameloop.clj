(ns engine.gameloop
  (:import (javax.swing Timer))
  (:require [engine.paint :as p]
            [engine.physics :as ph]
            [engine.ui :as ui]))

(defn start
  [state on-tick]
  (let [state                            (atom state)
        atom:keys                        (atom #{})
        step                             (atom 0)
        key-listener                     (ui/create-key-listener atom:keys)
        [buffer-strategy buffered-image] (ui/init-jframe 400 key-listener)
        fn:paint                         (p/paint buffer-strategy buffered-image)
        frame-listener                   (ui/create-frame-listener #(fn:paint (ph/get-engine-bodies)))
        physics-listener                 (partial ui/create-physics-listener
                                                  (fn [tick-in-ms]
                                                    (ph/step-in-ms)
                                                    (if (= @step 7)
                                                      (do
                                                        (reset! step 0)
                                                        (swap! state #(on-tick % @atom:keys tick-in-ms)))
                                                      (swap! step inc))))]
    (ui/looping frame-listener physics-listener 24 100)))
