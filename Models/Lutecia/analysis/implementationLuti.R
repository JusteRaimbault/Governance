
setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

resfile='20180504_114240_IMPL'

res <- as.tbl(read.csv(paste0('exploration/',resfile,'.csv')))

resdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',resfile,'/');dir.create(resdir)


#####
## distrib of differences by id
res %>% group_by(id,scenario) %>% summarise(count=n())
res %>% group_by(id,scenario) %>% summarise(avgdiff=mean(totaldiff))


g=ggplot(res,aes(x=totaldiff,color=paste0(id,scenario)))
g+geom_density()+scale_color_discrete(guide=F)+stdtheme
ggsave(file=paste0(resdir,"totaldiff_distrib.png"),width=15,height=10,units='cm')
# -> some high outliers, otherwise strongly centered around 0

# check confidence interval on the mean, if contains 0
sres = res %>% group_by(id,scenario) %>% summarise(mindiff=mean(totaldiff) - sd(totaldiff)*1.96/sqrt(n()),maxdiff=mean(totaldiff) + sd(totaldiff)*1.96/sqrt(n()))
which(!(sres$mindiff<0&&sres$maxdiff>0))
# -> ok, avg is always 0
summary(sres$maxdiff-sres$mindiff)

