
(ns dom.style
    (:require [css.api :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-style
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (get-element-style my-element)
  [element])
  ; ...

(defn set-element-style!
  ; @param (DOM-element) element
  ; @param (map) style
  ;
  ; @usage
  ;  (set-element-style! my-element {:position "fixed" :top "0"})
  [element style]
  (let [parsed-style (css/unparse style)]
       (.setAttribute element "style" parsed-style)))

(defn remove-element-style!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (remove-element-style! my-element)
  [element]
  (.removeAttribute element "style"))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-style-value
  ; @param (DOM-element) element
  ; @param (string) style-name
  ;
  ; @usage
  ;  (get-element-style my-element "position")
  ;
  ; @return (string)
  [element style-name]
  (-> js/window (.getComputedStyle element)
                (aget style-name)))

(defn set-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name
  ; @param (*) style-value
  ;
  ; @usage
  ;  (set-element-style-value! my-element "position" "fixed")
  [element style-name style-value]
  (-> element .-style (aset style-name style-value)))

(defn remove-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name

  ; @usage
  ;  (remove-element-style-value! my-element "position")
  [element style-name]
  (-> element .-style (aset style-name nil)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-computed-style
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (get-element-computed-style my-element)
  ;
  ; @return (CSSStyleDeclarationObject)
  ;  The returned object updates automatically when the element's styles are changed
  [element]
  (.getComputedStyle js/window element))
