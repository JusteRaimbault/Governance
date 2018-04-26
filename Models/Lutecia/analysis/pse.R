

# results PSE

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)
library(GGally)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

pseresdir='20180425_1511_PSE'
res <- as.tbl(read.csv('explo/20180425_1511_PSE/population1655.csv'))

resdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',pseresdir,'/');dir.create(resdir)




