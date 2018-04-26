
# correlations

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

#resfile='20180417_184334_SPACEMATTERS'
resfile='20180424_192346_CORRELATIONS'

res <- as.tbl(read.csv(paste0('exploration/',resfile,'.csv')))

figdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',resfile,'/');dir.create(figdir)



#####
##

# check time-series
res$frelocationRate=cut(res$relocationRate,5);res$fgammaCDA=cut(res$gammaCDA,5);res$fgammaCDE=cut(res$gammaCDE,5);res$scenario=as.character(res$scenario)
g=ggplot(res[res$tw==1&res$scenario=="0",],aes(x=t0,y=corAccessDev,colour=frelocationRate))
g+geom_point()+#geom_smooth()+
  facet_grid(fgammaCDA~fgammaCDE)

# -> too few parameter points


