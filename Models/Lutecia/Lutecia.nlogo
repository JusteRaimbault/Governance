
;;;;;;;;;;;;;;;;;;;;;
;;
;; LUTECIA Model
;;
;; --  Endogenous transportation governance in a model of co-evolution of transport and land-use --
;;
;;;;;;;;;;;;;;;;;;;;;

extensions[matrix table  nw shell gradient numanal gis morphology context] ;

__includes [

  ; main
  "main.nls"

  ; setup
  "setup.nls"



  ;;;;;;;;;
  ;; main modules
  ;;;;;;;;;

  ;; transportation
  "transportation.nls"

  ;; luti
  "luti.nls"

  ;; governance
  "governance.nls"

  ;;;;;;;;
  ; agents
  ;;;;;;;;

  ; mayors
  "mayor.nls"

  ; patches
  "patches.nls"

  ;;;;;;;;
  ; transportation network
  ;;;;;;;;

  ; network
  "network.nls"

  ; ghost network
  "network-ghost.nls"

  ; biological network generation
  "network-biological.nls"

  ;;;;;;;;;
  ; functions
  ;;;;;;;;;

  ; functions to update distance matrices
  "distances.nls"

  ; accessibilities
  "accessibilities.nls"

  ;;;;;;;;;;
  ; display
  ;;;;;;;;;;

  "display.nls"


  ;;;;;;;;;;
  ; indicators
  ;;;;;;;;;;

  ; main indics
  "indicators.nls"

  ; morphology
  "morphology.nls"

  ; network
  "network-indicators.nls"

  ; stylized facts
  "indicators-stylized.nls"


  ;;;;;;;;;;
  ;; visual exploration
  ;;;;;;;;;;

  "exploration.nls"

  ;;;;;;;;
  ;; experiments
  ;;;;;;;;

  ;; full experiments
  "experiments.nls"

  ;; specific luti experiments
  "experiments-luti.nls"



  ;;;;;;;;;;
  ;; utils
  ;;;;;;;;;;

  ; Q : package utils subpackages or all utils to have a simpler use ?

  "utils/math/SpatialKernels.nls"
  "utils/math/Statistics.nls"
  "utils/math/EuclidianDistanceUtilities.nls"
  "utils/math/Numanal.nls"
  "utils/misc/List.nls"
  "utils/misc/Types.nls"
  "utils/misc/Matrix.nls"
  "utils/misc/Table.nls"
  "utils/gui/Display.nls"
  "utils/agent/Link.nls"
  "utils/agent/AgentSet.nls"
  "utils/agent/Agent.nls"
  "utils/network/Network.nls"
  "utils/io/Timer.nls"
  "utils/io/Logger.nls"
  "utils/io/File.nls"
  "utils/misc/String.nls"

  ;;;;;;;;;;;
  ;; Tests
  ;;;;;;;;;;;

  "test/test.nls"
  "test/test-distances.nls"
  "test/test-transportation.nls"
  "test/test-experiments.nls"


]




globals[

  ;;;;;;;;;;;;;
  ;; Setup params
  ;;;;;;;;;;;;;

  ; initial number of territories
  ;global:#-initial-territories

  ; spatial distribution params
  ;global:actives-spatial-dispersion
  ;global:employments-spatial-distribution

  ;; global employments and actives list
  global:patches-employments-list
  global:patches-actives-list

  ;; convergence variables
  global:diff-actives
  global:diff-employments
  global:rel-diff-actives
  global:rel-diff-employments

  global:initial-max-acc

  ; utility : cobb-douglas parameter
  ;gamma-cobb-douglas

  ; relocation : discrete choice parameter
  ;beta-discrete-choices

  ; governor of the region : particular mayor
  global:regional-authority

  ;
  global:infra-snapping-tolerance ; \in [0,10] - default 1


  ; initial network
  ;initial-nw-random-type
  global:initial-nw?

  ;;
  ; slime mould initial network
  global:network-biological-steps
  global:network-biological-threshold
  global:network-biological-new-links-number
  global:network-biological-initial-diameter
  global:network-biological-diameter-max
  global:network-biological-total-diameter-variation
  global:network-biological-o
  global:network-biological-d
  global:network-biological-nodes-number
  global:network-biological-input-flow
  ;global:network-biological-gamma
  global:bio-ticks



  ;;
  ; externality

  global:with-externalities?
  ;; list of patches for the external facility
  global:external-facility
  ; endogenous growth of employments within the externality
  global:ext-growth-factor
  ; initial size of externality in employments
  global:ext-employments-proportion-of-max

  ;; coordinates of mayors, taken from setup file
  global:mayors-coordinates
  global:mayors-populations
  global:mayors-employments
  global:mayors-names

  ;; position of ext patch
  global:ext-position

  ;; path to the setup files
  global:positions-file
  global:ext-file

  ;; GIS setup
  global:gis-network-file
  global:gis-extent-file
  global:gis-centers-file
  global:gis-population-raster-file
  global:gis-sea-file
  global:gis-economic-areas-file
  global:gis-governed-patches-file

  ;conf-file

  ;;;;;;;;;;;;;
  ;; Transportation
  ;;;;;;;;;;;;;

  global:congestion-price
  global:lambda-flows

  ;; transportation flows \phi_ij between patches
  global:flow-matrix

  ;; congestion in patches
  ; list ordered by patch number
  global:patches-congestion

  ;; maximal pace (inverse of speed) in the transportation network
  ;network-max-pace
  global:euclidian-min-pace
  global:network-min-pace



  ;;;;;;;;;;;;;
  ;; governance
  ;;;;;;;;;;;;;

  global:collaborations-wanted
  global:collaborations-realized
  global:collaborations-expected

  global:beta-dc-game

  ;evolve-network?


  ;;;;;;;;;;;;;
  ;; Cached distances matrices
  ;;
  ;;  updated through dynamic programming rules
  ;;;;;;;;;;;;;

  ;; Matrix of euclidian distances between patches
  ; remains unchanged
  global:euclidian-distance-matrix

  ;; network distance (without congestion)
  global:network-distance-matrix

  ;; effective distance
  ;  - with congestion in network -
  global:effective-distance-matrix

  ;;
  ; Cached access patches to network, i.e. closest patch belonging to nw
  ;  @type table
  ;   number -> number of access
  global:nw-access-table

  ;; cached shortest paths -> updated same time as distance
  ; stored as table (num_patch_1,num_patch_2) -> [path-as-list]
  ;
  ; in network
  global:network-shortest-paths

  ;; list of nw patches
  ;  @type list
  ;  List of network patches number
  global:nw-patches

  ;; number of patches
  global:#-patches

  ;; for patches in nw, table caching closest nw inters (i.e. [end1,end2] of my-link )
  global:closest-nw-inters

  ;; network intersections
  ;  @type list
  ;  List of intersection patches numbers
  global:nw-inters

  ;; network clusters
  global:network-clusters

  ;; connexion between clusters
  global:network-clusters-connectors

  ; overall
  ; stored as table (num_patch_1,num_patch_2) -> [[i,i1],[i1,i2],...,[in,j]] where couples are either (void-nw) or (nw-nw)
  ; then effective path is [ik->i_k+1] or [ik->_nw i_k+1]
  global:effective-shortest-paths

  ;;
  ; maximal distance in the world
  global:dmax


  ;target-network-file

  ;;;
  ; network measures
  global:shortest-paths
  global:nw-relative-speeds
  global:nw-distances



  ;;;;;;;;;;;;;
  ;; Utils
  ;;;;;;;;;;;;;

  ; log level : defined in chooser
  ;log-level


  ;;;;;;;;;;;;;
  ;; Tests
  ;;;;;;;;;;;;;

  global:gridor

  ;; infra constructed by hand
  global:to-construct

  ;; HEADLESS
  global:headless?

  global:failed


  global:link-distance-function

  global:tracked-indicators
  global:history-indicators

  global:setup-from-world-file? ; only in experiments

]



patches-own [

  ; number of actives on the patch
  patch:actives

  ; number of jobs on the patch
  patch:employments

  ; variation of actives
  patch:delta-actives

  ; variation of employments
  patch:delta-employments


  ; number of the patch (used as index in distance matrices)
  patch:number

  ; pointer to governing mayor
  patch:governing-mayor

  ; actives and employment
  ; do not need mobile agents as deterministic evolution, considering at this time scale that random effect is averaged
  ;  on the contrary to transportation infrastructure evolution, that evolves at a greater scale.
  ;  -> patch variables and not agents






  ;;;;;
  ;; utilities and accessibilities
  ;;;;;

  ; accessibility of jobs to actives
  patch:a-to-e-accessibility

  ; accessibility of actives to employments
  patch:e-to-a-accessibility

  ; previous and current cumulated accessibilities
  patch:prev-accessibility
  patch:current-accessibility

  ; travel distances
  patch:a-to-e-distance
  patch:e-to-a-distance

  ; utilities
  ; for actives
  patch:a-utility
  ; for employments
  patch:e-utility

  ; form factor
  patch:form-factor


  patch:sea?


]


;;
; abstract entity representing territorial governance
breed[mayors mayor]

mayors-own[

  ; set of governed patches -> not needed ?
  ;governed-patches

  ; wealth of the area
  mayor:wealth

  mayor:population
  mayor:employment

]


;;;;;;;;;
;; Transportation Network
;;;;;;;;;

;; transportation link
undirected-link-breed[transportation-links transportation-link]

transportation-links-own [

  ;;
  ; link length
  transportation-link:length

  ;;
  ; betweenness centrality
  transportation-link:bw-centrality

  ;;
  ; capacity of the link ; expressed as max trip per length unit
  transportation-link:capacity

  ;;
  ; congestion : travels in the link
  transportation-link:congestion

  ;;
  ; speed in the link, deduced from capacity and congestion
  transportation-link:speed

  ;;
  ; tick on which the infra has been constructed
  transportation-link:age

  ;;
  ; status of the link
  transportation-link:status

]

;; nodes of the transportation network
breed[transportation-nodes transportation-node]

transportation-nodes-own[

  ;;
  ; node closeness centrality
  transportation-node:closeness-centrality
]


undirected-link-breed[ghost-transportation-links ghost-transportation-link]

breed[ghost-transportation-nodes ghost-transportation-node]



;;
; biological network generation

breed [biological-network-nodes biological-network-node]
breed [biological-network-poles biological-network-pole]

undirected-link-breed [biological-network-links biological-network-link]
undirected-link-breed [biological-network-real-links biological-network-real-link]

biological-network-nodes-own [
  ;; pressure
  biological-network-node:pressure
  ;; total capacity
  biological-network-node:total-capacity
  ;; number
  biological-network-node:number

  ;; population
  biological-network-node:population
]



biological-network-links-own [
  ;; diameter
  biological-network-link:diameter
  ;; flow
  biological-network-link:flow
  ;; length
  biological-network-link:bio-link-length
]
@#$#@#$#@
GRAPHICS-WINDOW
572
18
1012
479
7
7
28.666666666666668
1
10
1
1
1
0
0
0
1
-7
7
-7
7
0
0
1
ticks
30.0

SLIDER
3
73
186
106
global:#-initial-territories
global:#-initial-territories
0
5
5
1
1
NIL
HORIZONTAL

BUTTON
385
579
451
612
setup
setup:setup\n;luti luti display:color-patches
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

CHOOSER
13
660
165
705
global:patches-display
global:patches-display
"governance" "patch:actives" "patch:employments" "a-utility" "e-utility" "accessibility" "a-to-e-accessibility" "e-to-a-accessibility" "congestion" "mean-effective-distance" "lbc-effective-distance" "center-effective-distance" "lbc-network-distance" "network"
1

TEXTBOX
11
15
161
33
Setup parameters
11
0.0
1

TEXTBOX
15
248
165
266
Runtime parameters
11
0.0
1

SLIDER
3
120
284
153
global:actives-spatial-dispersion
global:actives-spatial-dispersion
0
10
1
0.1
1
NIL
HORIZONTAL

SLIDER
2
155
282
188
global:employments-spatial-dispersion
global:employments-spatial-dispersion
0
10
0.8
0.1
1
NIL
HORIZONTAL

SLIDER
285
155
483
188
global:actives-max
global:actives-max
0
1000
500
1
1
NIL
HORIZONTAL

SLIDER
286
120
486
153
global:employments-max
global:employments-max
0
1000
500
1
1
NIL
HORIZONTAL

SLIDER
7
290
250
323
global:gamma-cobb-douglas-a
global:gamma-cobb-douglas-a
0
1
0.9
0.01
1
NIL
HORIZONTAL

SLIDER
7
358
251
391
global:beta-discrete-choices
global:beta-discrete-choices
0
5
1.8
0.05
1
NIL
HORIZONTAL

BUTTON
512
579
567
612
go
main:go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
0

PLOT
1132
183
1355
338
convergence
NIL
NIL
0.0
2.0
0.0
1.0
true
false
"" ""
PENS
"default" 1.0 0 -5298144 true "" "plot global:rel-diff-employments / count patches"
"pen-1" 1.0 0 -12087248 true "" "plot global:rel-diff-actives / count patches"

OUTPUT
1071
567
1528
861
10

TEXTBOX
12
273
162
291
LUTI
11
0.0
1

TEXTBOX
336
261
486
279
Governance
11
0.0
1

SLIDER
285
278
530
311
global:regional-decision-proba
global:regional-decision-proba
0
1
0
0.05
1
NIL
HORIZONTAL

TEXTBOX
263
272
278
303
|
25
0.0
1

TEXTBOX
263
293
278
324
|
25
0.0
1

TEXTBOX
263
315
278
333
|
25
0.0
1

TEXTBOX
13
447
216
474
_________________
20
0.0
1

TEXTBOX
14
473
164
491
Transportation
11
0.0
1

TEXTBOX
263
326
278
357
|
25
0.0
1

CHOOSER
332
713
470
758
global:log-level
global:log-level
"DEBUG" "VERBOSE" "DEFAULT"
1

SLIDER
14
494
181
527
global:network-speed
global:network-speed
1
50
5
1
1
NIL
HORIZONTAL

SLIDER
285
312
530
345
global:road-length
global:road-length
1.0
20
1
1.0
1
NIL
HORIZONTAL

SLIDER
285
344
530
377
global:#-explorations
global:#-explorations
0
200
50
1
1
NIL
HORIZONTAL

SLIDER
7
392
251
425
global:lambda-accessibility
global:lambda-accessibility
0
0.01
0.001
0.0001
1
NIL
HORIZONTAL

BUTTON
724
618
885
651
compute indicators
indicators:compute-indicators
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
433
616
667
649
global:total-time-steps
global:total-time-steps
0
10000
228
1
1
NIL
HORIZONTAL

TEXTBOX
16
520
182
556
__________________
20
0.0
1

CHOOSER
283
414
435
459
global:game-type
global:game-type
"random" "simple-nash" "discrete-choices"
2

TEXTBOX
263
348
278
380
|
25
0.0
1

TEXTBOX
263
370
281
400
|
25
0.0
1

PLOT
1132
16
1355
182
accessibility
NIL
NIL
0.0
2.0
0.99
1.0
true
false
"" ""
PENS
"default" 1.0 0 -12186836 true "" "plot indicators:overall-mean-accessibility"

TEXTBOX
347
674
556
702
__________________
20
0.0
1

BUTTON
170
670
245
703
update
compute-patches-variables\ndisplay:color-patches
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
284
377
530
410
global:collaboration-cost
global:collaboration-cost
0
0.005
5.0E-4
1e-6
1
NIL
HORIZONTAL

CHOOSER
206
72
332
117
global:setup-type
global:setup-type
"random" "from-file" "gis-synthetic" "gis"
0

BUTTON
454
579
509
612
go
main:go
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
725
584
885
617
construct infrastructure
governance:manual-infrastructure-construction
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
7
324
250
357
global:gamma-cobb-douglas-e
global:gamma-cobb-douglas-e
0
1
0.8
0.05
1
NIL
HORIZONTAL

TEXTBOX
11
639
58
657
Display
11
0.0
1

SLIDER
0
33
101
66
global:seed
global:seed
-100000
100000
0
1
1
NIL
HORIZONTAL

SLIDER
100
34
203
67
global:world-size
global:world-size
0
50
15
1
1
NIL
HORIZONTAL

INPUTBOX
205
10
522
70
global:conf-file
setup/conf/synth_unbalanced_close.conf
1
0
String

PLOT
1357
16
1565
183
morphology
moran
slope
0.0
0.1
0.0
0.1
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plotxy indicators:morphology:moran-actives indicators:morphology:slope-actives"

SWITCH
14
583
209
616
global:evolve-network?
global:evolve-network?
1
1
-1000

SWITCH
14
550
210
583
global:evolve-landuse?
global:evolve-landuse?
0
1
-1000

PLOT
1357
185
1564
339
mean-travel-distance
NIL
NIL
0.0
10.0
75.0
85.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot indicators:mean-effective-distance"

INPUTBOX
481
705
605
765
global:target-network-file
setup/target/network0.shp
1
0
String

TEXTBOX
393
556
422
574
Run
11
0.0
1

TEXTBOX
721
560
871
578
Interactive
11
0.0
1

SLIDER
7
425
251
458
global:relocation-rate
global:relocation-rate
0
1
0.1
0.01
1
NIL
HORIZONTAL

CHOOSER
291
628
427
673
global:stopping-type
global:stopping-type
"time" "infrastructure-stock"
0

SLIDER
432
651
668
684
global:total-infrastructure-stock
global:total-infrastructure-stock
0
100
50
1
1
NIL
HORIZONTAL

TEXTBOX
700
583
715
613
|
25
0.0
1

TEXTBOX
700
607
715
637
|
25
0.0
1

TEXTBOX
700
625
734
653
|
25
0.0
1

TEXTBOX
700
647
715
677
|
25
0.0
1

TEXTBOX
263
390
278
420
|
25
0.0
1

TEXTBOX
263
413
278
443
|
25
0.0
1

TEXTBOX
263
435
278
465
|
25
0.0
1

CHOOSER
333
73
521
118
global:initial-nw-random-type
global:initial-nw-random-type
"tree-skeleton" "slime-mould" "full"
1

MONITOR
1542
583
1609
628
bio ticks
global:bio-ticks
17
1
11

SLIDER
3
190
256
223
global:network-biological-gamma
global:network-biological-gamma
0
10
1.4
0.1
1
NIL
HORIZONTAL

PLOT
1132
342
1354
492
cor-access-dev
NIL
NIL
0.0
1.0
-0.1
0.1
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot indicators-stylized:cor-access-dev"
"pen-1" 1.0 0 -2674135 true "" "plot 0"
"pen-2" 1.0 0 -13791810 true "" "plot indicators-stylized:cor-access-dev-mw"

PLOT
1357
341
1569
491
corr-access-employments
NIL
NIL
0.0
1.0
-0.1
0.1
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot indicators-stylized:cor-access-employments"
"pen-1" 1.0 0 -2674135 true "" "plot 0"
"pen-2" 1.0 0 -13791810 true "" "plot indicators:causal-moving-average \"indicators-stylized:cor-access-employments\" 15"

@#$#@#$#@
## Context

The LUTECIA model is a co-evolution model for land-use and transportation networks.

It is an extended LUTI model which currently implements the following sub-models :
 - Land-use evolution
 - Transportation flows (no traffic model yet)
 - Evaluation of Cooperation and Infrastructure provision
 - (not implemented : Agglomeration economies)

## Model description




## References

Raimbault, J. (2018). Characterizing and modeling the co-evolution of transportation networks and territories. PhD Thesis, Université Paris 7.

Le Néchet, F., & Raimbault, J. (2015, September). Modeling the emergence of metropolitan transport autorithy in a polycentric urban region. In European Colloqueum on Theoretical and Quantitative Geography.

Le Néchet, F. (2011, September). Urban dynamics modelling with endogeneous transport infrastructures, in a polycentric region. In 17th European Colloquium on Quantitative and Theoretical Geography.


## Versions


(MetropolSim v3.0)
Major changes since v2
   - matrix dynamic shortest path (euclidian and nw) computation
   - simplified population structure (one csp)
   - game-theoretical governance management

Possible extensions (v4) :
    * add different transportation modes ?
    * add csp ? not prioritary.
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

building institution
false
0
Rectangle -7500403 true true 0 60 300 270
Rectangle -16777216 true false 130 196 168 256
Rectangle -16777216 false false 0 255 300 270
Polygon -7500403 true true 0 60 150 15 300 60
Polygon -16777216 false false 0 60 150 15 300 60
Circle -1 true false 135 26 30
Circle -16777216 false false 135 25 30
Rectangle -16777216 false false 0 60 300 75
Rectangle -16777216 false false 218 75 255 90
Rectangle -16777216 false false 218 240 255 255
Rectangle -16777216 false false 224 90 249 240
Rectangle -16777216 false false 45 75 82 90
Rectangle -16777216 false false 45 240 82 255
Rectangle -16777216 false false 51 90 76 240
Rectangle -16777216 false false 90 240 127 255
Rectangle -16777216 false false 90 75 127 90
Rectangle -16777216 false false 96 90 121 240
Rectangle -16777216 false false 179 90 204 240
Rectangle -16777216 false false 173 75 210 90
Rectangle -16777216 false false 173 240 210 255
Rectangle -16777216 false false 269 90 294 240
Rectangle -16777216 false false 263 75 300 90
Rectangle -16777216 false false 263 240 300 255
Rectangle -16777216 false false 0 240 37 255
Rectangle -16777216 false false 6 90 31 240
Rectangle -16777216 false false 0 75 37 90
Line -16777216 false 112 260 184 260
Line -16777216 false 105 265 196 265

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

sheep
false
15
Circle -1 true true 203 65 88
Circle -1 true true 70 65 162
Circle -1 true true 150 105 120
Polygon -7500403 true false 218 120 240 165 255 165 278 120
Circle -7500403 true false 214 72 67
Rectangle -1 true true 164 223 179 298
Polygon -1 true true 45 285 30 285 30 240 15 195 45 210
Circle -1 true true 3 83 150
Rectangle -1 true true 65 221 80 296
Polygon -1 true true 195 285 210 285 210 240 240 210 195 210
Polygon -7500403 true false 276 85 285 105 302 99 294 83
Polygon -7500403 true false 219 85 210 105 193 99 201 83

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

wolf
false
0
Polygon -16777216 true false 253 133 245 131 245 133
Polygon -7500403 true true 2 194 13 197 30 191 38 193 38 205 20 226 20 257 27 265 38 266 40 260 31 253 31 230 60 206 68 198 75 209 66 228 65 243 82 261 84 268 100 267 103 261 77 239 79 231 100 207 98 196 119 201 143 202 160 195 166 210 172 213 173 238 167 251 160 248 154 265 169 264 178 247 186 240 198 260 200 271 217 271 219 262 207 258 195 230 192 198 210 184 227 164 242 144 259 145 284 151 277 141 293 140 299 134 297 127 273 119 270 105
Polygon -7500403 true true -1 195 14 180 36 166 40 153 53 140 82 131 134 133 159 126 188 115 227 108 236 102 238 98 268 86 269 92 281 87 269 103 269 113

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.3.1
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
