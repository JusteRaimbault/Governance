
# Methods for the exploration of Luti models

## Typical stylized facts

Luti models must reproduce several empirical stylized facts.

For most "classical" Luti models, empirical stylized facts include : (see (Wegener and Furst, 2004))
  - impact of LU on Transport :
      - correlation residential density / transportation mode
      - employment density => more public transport
      - neighborhood design (e.g. traditional vs car-oriented) influences trip length and modal shares
      - distance to public transport and employment centers => model shares
      - larger cities have highest public transport model share and shorter trips
  - impact of Transport on LU :
      - more accessible areas are developed faster
      - employments locate in highly accessible areas
      - good accessibility => suburban dispersal => longer trips
  - transport :
      - travel cost / travel time => modal choice
      - travel time improvement => more and longer trips (Zahavi law)

Literature review on Luti: see [(Raimbault, 2018) - ch2](Raimbault_Memoire_v3.5.3_p107-p162.pdf)


## Specific stylized facts

We will consider in particular models with the following structure :
  - aggregated population and employments temporal trajectories are fixed (with data or as synthetic inputs)
  - location choices are estimated on initial data
  - local pop and employments, and urban growth (densities) are simulated, and put in correspondence with disaggregated macro data (unclear what is the role of disaggregation)


TODO: ontological correspondence between targeted models and toy model.


## Toy Luti example

The Lutecia model (Le Nechet and Raimbault, 2015) is a co-evolution model for land-use and transportation networks, which includes endogenous governance processes for the evolution of transportation networks. It couples a basic Luti model with a dynamic transportation network. It can be run with a fixed network and used as a toy luti model for proof-of-concept of exploration methods.

Description / basic exploration of the Lutecia model: see [(Raimbault, 2018) - ch7.3](../Lutecia/Raimbault_Memoire_v3.5.3_p401-p444.pdf)


## Indicators

For the toy example we consider, the LU-T modules (model with static transportation network) can be studied with the following indicators (on which ranges or values can be targeted) :
  - indicators of urban form for actives and employments (moran, entropy, average distance, hierarchy)
  - temporal scale for urban sprawl
  - length/cost of trips ; transportation flows
  - distribution of accessibility
  - on stylized urban configurations (e.g. two centers) : evolution of centers relative shares


## Examples of possible explorations

(for the toy luti with fixed network)

### Feasible urban configurations


  - we want to obtain a "sustainable" urban configuration (defined for example in our model with a low average transport length/cost) : which model parameter values can lead to it, given an initial configuration (= feasible urban trajectories) ; more generally which initial configuration (meta-parameters) and parameters ? -> application of the reverse image algo
  - how to "validate" the model, in the sense of ranges for some indicators corresponding to stylized facts ?
  - similar question but maybe more relevant the 4city applications : what are optimal urban configurations for a company which want to relocate ?


### Spatial sensitivity of the model

  - how do perturbation in the transportation network influence outcomes ? (-> e.g. robustness of sustainibility)
  - how do perturbation of the urban form influence outcomes ?
  - (more a theoretical question, in line with my thesis questionings) how do *correlated* perturbation influence outcomes ?
      - how do we generate correlated perturbations ?
      - do static correlation structures correspond to dynamic interactions ?
      - are some correlation / interaction structures "better" given some indicators ?
