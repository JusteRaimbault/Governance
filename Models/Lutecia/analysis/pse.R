

# results PSE

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)
library(GGally)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

#pseresdir='20180425_1511_PSE'
pseresdir='20180430_2049_PSE'
res <- as.tbl(read.csv('explo/20180430_2049_PSE/population7955.csv'))
#res=res[res$evolution.samples>5,]

figdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',pseresdir,'/');dir.create(figdir)



###
# scatterplots of outputs

#indicators=c("corAccessDev","corAccessEmployments","corAccessTimeWeighted","corAccessTimeUnweighted")
indicators=c("relativeAccessibility","relativeCongestion")

res$relocationRate = cut(res$relocationRate,6)
ggpairs(data=res,columns = indicators,
        aes(colour=relocationRate,alpha=0.4)
)
ggsave(filename = paste0(figdir,'scatterplot_colorrelocationRate.png'),width=40,height=25,units='cm')

# rq : 200^4 = 1.6e9 -> memory fail for correlations

###
# Convergence

pops = read.csv(paste0('explo/',pseresdir,'/counts.csv',sep=';'))
names(pops)=c("pop","time")
summary(lm(data=pops,pop~time))
plot(pops[,2],pops[,1],type='l')



