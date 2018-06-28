
setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia/model/netlogo6'))

library(dplyr)

dmapnl6 <- as.tbl(read.csv('res/dmap_nl6.csv',sep=';'))
dmapsc <- as.tbl(read.csv('res/dmap_sc1.csv',sep=';',header=F)[,2:4])
#dmapsc <- as.tbl(read.csv('res/dmap_sc2.csv',sep=';',header=F))

joined = left_join(dmapnl6,dmapsc,by=c('i'='V2','j'='V3'))

sum(joined[abs(joined$d - joined$V4) > 2,4])

100 * 2 * sum(abs(joined$d - joined$V4)) / (sum(joined$d) + sum(joined$V4))
# 5.091637 for worldSize = 15
# 5.19 for worldSize = 25
#  -> asymptotic value ?



