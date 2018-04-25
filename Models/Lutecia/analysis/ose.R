
# results OSE / compare with direct sampling

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)
library(GGally)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

resfile='20180420_183531_DIRECT_SAMPLING'
res <- as.tbl(read.csv(paste0('exploration/',resfile,'.csv')))

oseresdir='20180424_1559_OSE'
oseres <- as.tbl(read.csv('explo/20180424_1559_OSE/population4177.csv'))

resdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',oseresdir,'/');dir.create(resdir)


###

# check outputs covering
#plot(oseres$opositeRelativeAccessibility,oseres$nwCongestion)

oseres$scenario=as.character(oseres$scenario)

params=c("relocationRate","gammaCDA","gammaCDE","betaDC","scenario")

png(filename = paste0(resdir,'indics_colorparams.png'),width = 60,height=30,units='cm',res=300)
plots=list()
for(param in params){
  g=ggplot(oseres,aes_string(x="opositeRelativeAccessibility",y="nwCongestion",colour=param))
  plots[[param]]=g+geom_point()
}
multiplot(plotlist = plots,cols=3)
dev.off()



# input plots
oseres$opositeRelativeAccessibility=cut(oseres$opositeRelativeAccessibility,5)
ggpairs(data=oseres,columns = params,
        aes(colour=opositeRelativeAccessibility)
)
ggsave(filename = paste0(resdir,'scatterparams_color-relAcc.png'),width = 60,height=30,units='cm')

oseres$nwCongestion=cut(oseres$nwCongestion,5)
ggpairs(data=oseres,columns = params,
        aes(colour=nwCongestion)
)
ggsave(filename = paste0(resdir,'scatterparams_color-nwCongestion.png'),width = 60,height=30,units='cm')


