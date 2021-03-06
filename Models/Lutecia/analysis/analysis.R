
library(dplyr)
library(ggplot2)

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

source('analysis/functions.R')


#resdir = '20170508_lhs/'
#res <- as.tbl(read.csv(file = '20170508_lhs/data/20170508_190418_lhs.csv',sep=',',header=T))

resdir = '20170528_real/'
res <- as.tbl(read.csv(file = paste0(Sys.getenv('CN_HOME'),'/Results/Governance/20170528_real/data/20170528_grid_real_full.csv'),sep=',',header=F,stringsAsFactors = F,skip = 1))


finalTime = 10
names(res)<-namesTS(c("accessibilityTS","betaDC","centreActivesPropTS","centreEmploymentsPropTS"
                      ,"collcost","constrcost","entropyActivesTS","entropyEmploymentsTS",
                      "euclpace","evolveLanduse","evolveNetwork","expcollab","failed","finalTime","game","gametype",
                      "gammaCDA","gammaCDE","id","lambdaAcc","maxFlowTS","meanDistanceActivesTS",
                      "meanDistanceCentreActivesTS","meanDistanceCentreEmploymentsTS","meanDistanceEmploymentsTS",
                      "meanFlowTS","minFlowTS","moranActivesTS","moranEmploymentsTS","nwBetweenness",
                      "nwCloseness","nwDiameter","nwLength","nwPathLength","nwRelativeSpeed","realcollab",
                      "regionalProba","replication","setupType","slopeActivesTS","slopeEmploymentsTS","slopeRsquaredActivesTS",
                      "slopeRsquaredEmploymentsTS","stabilityTS","synthConfFile","targetDistance","targetNetwork",
                      "traveldistanceTS","wantedcollab"
),finalTime)


params = c("collcost","regionalProba","game")

##
# added for reproduction by new simulations

reprod <- as.tbl(rbind(
  read.csv(file = 'openmole/calibration/20210212_160546_CALIBRATIONLHS_LANDUSE-RELOCRATE1_GRID.csv',sep=',',stringsAsFactors = F),
  read.csv(file = 'openmole/calibration/20210213_131315_CALIBRATIONLHS_LANDUSE-RELOCRATE1_GRID.csv',sep=',',stringsAsFactors = F),
  read.csv(file = 'openmole/calibration/r.csv',sep=',',stringsAsFactors = F)
  )
)
reprodnolu <- as.tbl(read.csv(file = 'openmole/calibration/20210212_160409_CALIBRATIONLHS_NOLANDUSE_GRID.csv',sep=',',stringsAsFactors = F))
# res = reprod
# res = reprodnolu

####
# TODO : 
#   - specific calib on bridges ?
#   - random network null model ?


####
# Minimal target dist value when aggreg on repetitions
sres = res %>% group_by(id) %>% summarise(count=n())
length(sres$id[sres$count>=4])/nrow(sres)
ids = sres$id[sres$count>=4]

dim(res[res$id%in%ids,] %>% group_by(id) %>% summarise(count=n()))
sres = res[res$id%in%ids,] %>% group_by(collcost,regionalProba,game,constrcost) %>% summarise(distance=mean(targetDistance))
sres[sres$distance==min(sres$distance),]
# 0.00379         0.972  1.31    0.00888     79.2
# note: l_r = 2 fixed in this experiment, with workd size 15

##
# hist of obj

g=ggplot(res,aes(x=targetDistance,..density..))
g+geom_histogram(bins = 50)+geom_density(col='red',adjust=2)+xlab("Distance to real network")
ggsave(filename = paste0(resdir,'hist_targetDistance.png'),width = 15,height=10,units = 'cm')


## distance as function of collaboration

g=ggplot(res,aes(x=regionalProba,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()
ggsave(filename = paste0(resdir,'regional-distance_colorrealcollab.png'),width = 15,height=10,units = 'cm')

g=ggplot(res[res$targetDistance<40,],aes(x=regionalproba,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()
ggsave(filename = paste0(resdir,'regional-distance_colorrealcollab_less40.png'),width = 15,height=10,units = 'cm')


# color by game type ?
g=ggplot(res,aes(x=regionalproba,y=targetDistance,color=gametype))
g+geom_point(pch='.')+stat_smooth()
ggsave(filename = paste0(resdir,'regional-distance_colorgametype.png'),width = 15,height=10,units = 'cm')


g=ggplot(res,aes(x=gametype,y=targetDistance,fill=gametype))
g+geom_violin(draw_quantiles = c(0.25,0.5,0.75))
ggsave(filename = paste0(resdir,'distanceviolin_gametype.png'),width = 15,height=10,units = 'cm')



sres = res%>%group_by(id)%>%summarise(targetDistance=mean(targetDistance),realcollab=mean(realcollab))
g=ggplot(sres,aes(x=realcollab,y=targetDistance))
g+geom_point(pch='.')+stat_smooth()

sres = res%>%group_by(realcollab)%>%summarise(meanTargetDistance=mean(targetDistance),sdTargetDistance=sd(targetDistance))
g=ggplot(res,aes(x=realcollab,y=targetDistance,color=regionalProba))
g+geom_point()+#stat_smooth(n = 10000)
  geom_point(data=sres,aes(x=realcollab,y=meanTargetDistance),col='red')+
  geom_line(data=sres,aes(x=realcollab,y=meanTargetDistance),col='red')+
  geom_errorbar(data=sres,aes(x=realcollab,y=meanTargetDistance,ymin=meanTargetDistance-sdTargetDistance,ymax=meanTargetDistance+sdTargetDistance),col='red')
ggsave(filename = paste0(resdir,'collab-distance_colorregional.png'),width = 15,height=10,units = 'cm')


## does regional proba/ collaboration level increase accessibility ?

#g=ggplot(res,aes(x=regionalproba,y=accessibility,color=realcollab))
g=ggplot(res,aes(x=regionalproba,y=accessibilityTS10,color=realcollab))
g+geom_point()+stat_smooth()
# NOTHING !!
ggsave(filename = paste0(resdir,'regional-access_colorrealcollab.png'),width = 15,height=10,units = 'cm')

#sres = res%>%group_by(realcollab)%>%summarise(meanAccessibility=mean(accessibility),sdAccessibility=sd(accessibility))
sres = res%>%group_by(realcollab)%>%summarise(meanAccessibility=mean(accessibilityTS10),sdAccessibility=sd(accessibilityTS10))
#g=ggplot(res,aes(x=realcollab,y=accessibility,color=regionalproba))
g=ggplot(res,aes(x=realcollab,y=accessibilityTS10,color=regionalproba))
g+geom_point()+
  geom_point(data=sres,aes(x=realcollab,y=meanAccessibility),col='red')+geom_line(data=sres,aes(x=realcollab,y=meanAccessibility),col='red')+
  geom_errorbar(data=sres,aes(x=realcollab,y=meanAccessibility,ymin=meanAccessibility-sdAccessibility,ymax=meanAccessibility+sdAccessibility),col='red')



## lambda accessibility
g=ggplot(res,aes(x=lambdaAcc,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()
ggsave(filename = paste0(resdir,'lambdaacc-distance_colorrealcollab.png'),width = 15,height=10,units = 'cm')

## eucl pace
# (close to 1 should yield random network ?)
#

g=ggplot(res,aes(x=euclpace,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()
ggsave(filename = paste0(resdir,'euclpace-distance_colorrealcollab.png'),width = 15,height=10,units = 'cm')


## constr/coll costs rate
g=ggplot(res[res$collcost/res$constrcost<10,],aes(x=collcost/constrcost,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()

g=ggplot(res,aes(x=collcost,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()

g=ggplot(res,aes(x=collcost,y=targetDistance,color=gametype))
g+geom_point()+stat_smooth()

g=ggplot(res,aes(x=constrcost,y=targetDistance,color=realcollab))
g+geom_point()+stat_smooth()

# at least influence on collaboration rate ?
g=ggplot(res,aes(x=constrcost,y=expcollab,color=targetDistance))
g+geom_point()+stat_smooth()+facet_wrap(~gametype)



g=ggplot(res,aes(x=regionalproba,y=realcollab,color=gametype))
g+geom_point(pch='.')+stat_smooth()
ggsave(filename = paste0(resdir,'regionalproba-realcollab_colorgametype.png'),width = 18,height=10,units = 'cm')

g=ggplot(res,aes(x=regionalproba,y=expcollab,color=gametype))
g+geom_point(pch='.')+stat_smooth()
ggsave(filename = paste0(resdir,'regionalproba-expcollab_colorgametype.png'),width = 18,height=10,units = 'cm')


