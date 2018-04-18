
setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

res <- as.tbl(read.csv('exploration/20180417_184334_SPACEMATTERS.csv'))

# counts
res %>% group_by(id,scenario) %>% summarize(count=n())
res %>% group_by(id) %>% summarize(count=length(unique(scenario)))
res %>% group_by(betaDC,gammaCDA,gammaCDE,lambdaAcc,relocationRate,networkSpeed) %>% summarize(scenarios=length(unique(scenario)),count=n())

# add a parameter id : pb with the behavior of withIndex ?
res = res %>% mutate(paridraw=paste0(betaDC,gammaCDA,gammaCDE,lambdaAcc,relocationRate,networkSpeed)) %>% mutate(parid = match(paridraw,unique(paridraw)))

#restest counts
res %>% group_by(parid) %>% summarize(scenarios=length(unique(scenario)),count=n())



# histograms
g = ggplot(res[res$parid==1,],aes(x=relativeAccessibility,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)

# test rough confidence intervals with estimated sigma
sres = res %>% group_by(id,scenario) %>% summarize(parid= parid[1] ,avg = mean(relativeAccessibility),ci = sd(relativeAccessibility)*1.96/sqrt(n()))

g = ggplot(sres[sres$id%in%1:10,])
g+geom_vline(aes(xintercept=avg,colour=paste0(id,scenario)))+geom_vline(aes(xintercept=avg-ci,colour=paste0(id,scenario)),linetype=2)+geom_vline(aes(xintercept=avg+ci,colour=paste0(id,scenario)),linetype=2)

#full plots

parids=c(1,2)

sres = res %>% group_by(id,scenario) %>% summarize(parid= parid[1] ,avg = mean(relativeAccessibility),ci = sd(relativeAccessibility)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=relativeAccessibility,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)

sres = res %>% group_by(id,scenario) %>% summarize(parid= parid[1] ,avg = mean(nwBetweenness),ci = sd(nwBetweenness)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=nwBetweenness,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)

sres = res %>% group_by(id,scenario) %>% summarize(parid= parid[1] ,avg = mean(moranActives),ci = sd(moranActives)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=moranActives,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)

sres = res %>% group_by(id,scenario) %>% summarize(parid= parid[1] ,avg = mean(nwRelativeLength),ci = sd(moranActives)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=nwRelativeLength,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)


m1 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+lambdaAcc+relocationRate+networkSpeed,data=res)
m2 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+lambdaAcc+relocationRate+networkSpeed+as.factor(scenario),data=res)
summary(m1)
summary(m2)
anova(m1)
anova(m2)
anova(m1,m2)
cor(res[,c("betaDC","gammaCDA","gammaCDE","lambdaAcc","relocationRate","networkSpeed","scenario")])

##
for(indic in c("relativeAccessibility","nwBetweenness","nwRelativeLength","nwRelativeSpeed","moranActives","meanDistanceActives")){
  m = lm(paste0(indic,"~betaDC+gammaCDA+gammaCDE+lambdaAcc+relocationRate+networkSpeed+as.factor(scenario)"),data=res)
  show(anova(m))
}

#  -> relativeAccessibility is the relevant variable with comparable variance for params / scenario / repetitions 

ss = anova(m2)$`Sum Sq`;names(ss)<-rownames(anova(m2))
100*ss / sum(ss)




