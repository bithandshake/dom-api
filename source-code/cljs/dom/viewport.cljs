
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.viewport
    (:require [dom.config        :as config]
              [mid-fruits.candy  :refer [return]]
              [mid-fruits.map    :as map]
              [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @usage
  ;  (get-viewport-height)
  ;
  ; @return (px)
  []
  (.-innerHeight js/window))

(defn get-viewport-width
  ; @usage
  ;  (get-viewport-width)
  ;
  ; @return (px)
  []
  (.-innerWidth js/window))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn viewport-profile-match?
  ; @param (keyword) profile
  ;  :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;
  ; @usage
  ;  (viewport-profile-match? :xl)
  ;
  ; @return (boolean)
  [profile]
  (let [viewport-width (.-innerWidth js/window)]
       (and (>= viewport-width (get-in config/VIEWPORT-PROFILES [profile :min]))
            (<= viewport-width (get-in config/VIEWPORT-PROFILES [profile :max])))))

(defn viewport-profiles-match?
  ; @param (keywords in vector) profiles
  ;  [:xxs, :xs, :s, :m, :l, :xl, :xxl]
  ;
  ; @usage
  ;  (viewport-profiles-match? [:xs :s :m])
  ;
  ; @return (boolean)
  [profiles]

  ; Alternative:
  ; (vector/contains-item? profiles (get-viewport-profile))

  (vector/any-item-match? profiles viewport-profile-match?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-profile
  ; @usage
  ;  (get-viewport-profile)
  ;
  ; @return (keyword)
  ;  :xxs, :xs, :s, :m, :l, :xl, :xxl
  []
  (let [viewport-width (.-innerWidth js/window)]
       (letfn [(f [{:keys [min max]}]
                  (and (>= viewport-width min)
                       (<= viewport-width max)))]
              (map/get-first-match-key config/VIEWPORT-PROFILES f))))

(defn get-viewport-orientation
  ; @usage
  ;  (get-viewport-orientation)
  ;
  ; @return (keyword)
  ;  :landscape, :portrait
  []
  (if (> (.-innerHeight js/window)
         (.-innerWidth  js/window))
      (return :portrait)
      (return :landscape)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn square-viewport?
  ; @usage
  ;  (square-viewport?)
  ;
  ; @return (boolean)
  []
  (= (.-innerHeight js/window)
     (.-innerWidth  js/window)))

(defn landscape-viewport?
  ; @usage
  ;  (landscape-viewport?)
  ;
  ; @return (boolean)
  []
  (< (.-innerHeight js/window)
     (.-innerWidth  js/window)))

(defn portrait-viewport?
  ; @usage
  ;  (portrait-viewport?)
  ;
  ; @return (boolean)
  []
  (> (.-innerHeight js/window)
     (.-innerWidth  js/window)))
