
(ns dom.body)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-element
  ; @usage
  ; (get-body-element)
  ;
  ; @return (DOM-element)
  []
  (-> js/document (.getElementsByTagName "body")
                  (aget 0)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-style-value
  ; @param (string) style-name
  ;
  ; @usage
  ; (get-body-style-value "background-color")
  ;
  ; @return (string)
  [style-name]
  (-> js/window (.getComputedStyle (-> js/document (.getElementsByTagName "body")
                                                   (aget 0)))
                (aget style-name)))
