
(ns dom.scroll
    (:require [candy.api  :refer [return]]
              [dom.config :as config]
              [math.api   :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scroll-x
  ; @usage
  ; (get-scroll-x)
  ;
  ; @return (px)
  []
  (-> js/document .-documentElement .-scrollLeft))

(defn get-scroll-y
  ; @usage
  ; (get-scroll-y)
  ;
  ; @return (px)
  []
  (-> js/document .-documentElement .-scrollTop))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-direction-ttb?
  ; @param (integer) last-scroll-y
  ;
  ; @usage
  ; (scroll-direction-ttb? 100)
  ;
  ; @return (boolean)
  [last-scroll-y]
  ; Ha a scroll-y értéke legalább a config.SCROLL-DIRECTION-SENSITIVITY
  ; értékével nagyobb, mint a last-scroll-y értéke ...
  (< (+ last-scroll-y config/SCROLL-DIRECTION-SENSITIVITY)
     (-> js/document .-documentElement .-scrollTop)))

(defn scroll-direction-btt?
  ; @param (integer) last-scroll-y
  ;
  ; @usage
  ; (scroll-direction-btt? 100)
  ;
  ; @return (boolean)
  [last-scroll-y]
  ; Ha a scroll-y értéke legalább a config.SCROLL-DIRECTION-SENSITIVITY
  ; értékével kisebb, mint a last-scroll-y értéke ...
  (> (- last-scroll-y config/SCROLL-DIRECTION-SENSITIVITY)
     (-> js/document .-documentElement .-scrollTop)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scroll-direction
  ; @param (integer) last-scroll-y
  ;
  ; @usage
  ; (get-scroll-direction 100)
  ;
  ; @return (keyword or nil)
  ;  nil, :btt, :ttb
  [last-scroll-y]
             ; XXX#0061
  (cond (and (scroll-direction-ttb? last-scroll-y)
             (math/nonnegative?     last-scroll-y))
        (return :ttb)

             ; XXX#0061
        (and (scroll-direction-btt? last-scroll-y)
             (math/nonnegative?     last-scroll-y))
        (return :btt)

        ; XXX#0061
        ; Bizonyos böngészőknél, a "scroll bounce effect" az oldal
        ; tetejére nagy lendülettel hirtelen visszagörgetéskor,
        ; a bounce közben – amikor már a scroll-y értéke
        ; a 0 felé közelít negatív irányból – a scroll-direction értékét
        ; :ttb-ként állítaná be, miközben az eredeti gesztus a felfelé görgetés.
        (math/negative? last-scroll-y)
        (return :btt)

        ; XXX#0088
        ; Ha a last-scroll-y és scroll-y értékének különbségének abszolút értéke
        ; nem nagyobb, mint a SCROLL-DIRECTION-SENSITIVITY és nem igaz az XXX#0061
        ; kivétel, akkor a scroll-direction nem megállapítható
        :return nil))

(defn get-scroll-progress
  ; @usage
  ; (get-scroll-progress)
  ;
  ; @return (percent)
  ; 0 - 100
  []
  (let [viewport-height (-> js/window   .-innerHeight)
        scroll-y        (-> js/document .-documentElement .-scrollTop)
        document-height (-> js/document .-documentElement .-scrollHeight)
        max-scroll-y    (- document-height viewport-height)
        scroll-progress (math/percent max-scroll-y scroll-y)]
      ; A DOM-struktúra felépülése közben előfordul olyan pillanat, amikor
      ; a document-height értéke nem valós, ebből kifolyólag a scroll-progress
      ; értéke ilyenkor kisebb lenne, mint 0.
      (math/between! scroll-progress 0 100)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-scroll-x!
  ; @param (px) scroll-x
  ; @param (map)(opt) options
  ; {:smooth? (boolean)
  ;   Default: false}
  ;
  ; @usage
  ; (set-scroll-x! 100)
  ([scroll-x]
   (set-scroll-x! scroll-x {}))

  ([scroll-x {:keys [smooth?]}]
   ; BUG#8709
   ; Out of order!
   ; (let [scroll-behavior   (if smooth? "smooth" "auto")
   ;       scroll-to-options {"left" scroll-x "top" 0 "behavior" scroll-behavior}]
   ;      (.scrollBy js/window (clj->js scroll-to-options)))
   (-> js/document .-documentElement .-scrollLeft (set! scroll-x))))

(defn set-scroll-y!
  ; @param (px) scroll-y
  ; @param (map)(opt) options
  ; {:smooth? (boolean)
  ;   Default: false}
  ;
  ; @usage
  ; (set-scroll-y! 100)
  ([scroll-y]
   (set-scroll-y! scroll-y {}))

  ([scroll-y {:keys [smooth?]}]
   ; BUG#8709
   ; Out of order!
   ; (let [scroll-behavior   (if smooth? "smooth" "auto")
   ;       scroll-to-options {"left" 0 "top" scroll-y "behavior" scroll-behavior}]
   ;      (.scrollBy js/window (clj->js scroll-to-options)))
   (-> js/document .-documentElement .-scrollTop (set! scroll-y))))

(defn scroll-to-element-top!
  ; @param (DOM-element) element
  ; @param (px)(opt) offset
  ;
  ; @usage
  ; (def my-element (get-element-by-id "my-element"))
  ; (scroll-to-element-top! my-element)
  ([element]
   (scroll-to-element-top! element 0))

  ([element offset]
   (-> js/document .-documentElement .-scrollTop
       (set! (+ offset (-> element     .getBoundingClientRect .-top)
                       (-> js/document .-documentElement      .-scrollTop))))))
