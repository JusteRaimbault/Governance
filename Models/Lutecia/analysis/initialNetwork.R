
setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

resfile='20180508-20180516_INITIAL_NETWORK'
resdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/BioNetwork/',resfile,'/');dir.create(resdir)


res <- read.csv('exploration/20180508_162131_INITIAL_NETWORK.csv',dec = '.',sep=',')
#res=rbind(res,read.csv('exploration/20180515_122503_INITIAL_NETWORK.csv',dec = '.',sep=','))
res=rbind(res,read.csv('exploration/20180516_134629_INITIAL_NETWORK.csv',dec = '.',sep=','))
res=as.tbl(res)
# remove nulls
res=res[res$nwTypeName!='null',]
# make numeric
res$nwRelativeLength = as.numeric(as.character(res$nwRelativeLength))
res$nwRelativeSpeed = as.numeric(as.character(res$nwRelativeSpeed))
# remove empty networks
res = res[res$nwRelativeLength>0&res$nwRelativeSpeed>0&!is.na(res$nwTypeName),]

quantile(res$nwRelativeLength,0.95)
#95% 
#1.271054 

quantile(res$nwRelativeSpeed,0.95)
#95% 
#1.777469

# -> filter extreme values
res = res[res$nwRelativeLength<quantile(res$nwRelativeLength,0.95)&res$nwRelativeSpeed<quantile(res$nwRelativeSpeed,0.95),]

#rq : could control by Urban Form ?

summary(res[,c("nwRelativeLength","nwRelativeSpeed","nwTypeName")])

res$gamma = as.numeric(res$nwTypeName=='slime-mould')*res$gammaSlimeMould + as.numeric(res$nwTypeName!='slime-mould')

# pareto plot for optimality
g=ggplot(res,aes(x=nwRelativeSpeed,y=nwRelativeLength,color=nwTypeName,size=gamma))
g+geom_point(alpha=0.4)+xlab("Network performance")+ylab("Network relative length")+
  scale_color_discrete(name="Network type")+scale_size_continuous(name=expression(gamma))+
  stdtheme
ggsave(file=paste0(resdir,'paretoSpeedLength.png'),width=24,height = 20,units='cm')










