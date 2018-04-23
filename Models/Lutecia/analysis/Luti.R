
setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia'))

library(dplyr)
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/CityNetwork/Models/Utils/R/plots.R'))

#resfile='20180417_184334_SPACEMATTERS'
resfile='20180420_183531_DIRECT_SAMPLING'

res <- as.tbl(read.csv(paste0('exploration/',resfile,'.csv')))

resdir = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Luti/',resfile,'/');dir.create(resdir)


# counts
res %>% group_by(id) %>% summarize(count=n())
res %>% group_by(id,scenario) %>% summarize(count=n())
res %>% group_by(id) %>% summarize(count=length(unique(scenario)))
res %>% group_by(betaDC,gammaCDA,gammaCDE,lambdaAcc,relocationRate,networkSpeed) %>% summarize(scenarios=length(unique(scenario)),count=n())

# add a parameter id : pb with the behavior of withIndex ?
#res = res %>% mutate(paridraw=paste0(betaDC,gammaCDA,gammaCDE,lambdaAcc,relocationRate,networkSpeed)) %>% mutate(parid = match(paridraw,unique(paridraw)))
res=res %>% mutate(parid=id)

#restest counts
res %>% group_by(parid) %>% summarize(scenarios=length(unique(scenario)),count=n())



# histograms
#g = ggplot(res[res$parid==1,],aes(x=relativeAccessibility,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
#g+geom_density(alpha=0.4)

# test rough confidence intervals with estimated sigma
#sres = res %>% group_by(parid,scenario) %>% summarize(avgRelativeAccessibility = mean(relativeAccessibility),ci = sd(relativeAccessibility)*1.96/sqrt(n()))
#g = ggplot(sres[sres$parid%in%1:3,])
#g+geom_vline(aes(xintercept=avgRelativeAccessibility,colour=paste0(parid,"-",scenario)))+geom_vline(aes(xintercept=avgRelativeAccessibility-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(aes(xintercept=avgRelativeAccessibility+ci,colour=paste0(parid,"-",scenario)),linetype=2)

#full plots : distributions and averages

parids=c(1,2)

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(relativeAccessibility),ci = sd(relativeAccessibility)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=relativeAccessibility,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(tilde(X)))+stdtheme
ggsave(filename = paste0(resdir,'distrib_relativeAccessibility.png'),width=15,height=10,units='cm')

#sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(nwBetweenness),ci = sd(nwBetweenness)*1.96/sqrt(n()))
#g = ggplot(res[res$parid%in%parids,],aes(x=nwBetweenness,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
#g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
#  scale_color_discrete(name="id-scenario")+xlab(expression(bar(bw)))+stdtheme
#ggsave(filename = paste0(resdir,'distrib_nwBetweenness.png'),width=15,height=10,units='cm')

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(nwCongestion),ci = sd(nwCongestion)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=nwCongestion,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab("congestion")+stdtheme
ggsave(filename = paste0(resdir,'distrib_nwCongestion.png'),width=15,height=10,units='cm')



sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(moranActives),ci = sd(moranActives)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=moranActives,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(I*"("*A*")"))+stdtheme
ggsave(filename = paste0(resdir,'distrib_moranActives.png'),width=15,height=10,units='cm')


sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(nwRelativeLength),ci = sd(nwRelativeLength)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=nwRelativeLength,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(L))+stdtheme
ggsave(filename = paste0(resdir,'distrib_networkLength.png'),width=15,height=10,units='cm')


###############
## Anovas to check relative parameters influence


#m1 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+lambdaAcc+relocationRate+networkSpeed,data=res)
m1 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+relocationRate,data=res)
#m2 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+lambdaAcc+relocationRate+networkSpeed+as.factor(scenario),data=res)
m2 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+relocationRate+as.factor(scenario),data=res)
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
m2 = lm(relativeAccessibility~betaDC+gammaCDA+gammaCDE+relocationRate+as.factor(scenario),data=res)
ss = anova(m2)$`Sum Sq`;names(ss)<-rownames(anova(m2))
100*ss / sum(ss)

m2 = lm(nwCongestion~betaDC+gammaCDA+gammaCDE+relocationRate+as.factor(scenario),data=res)
ss = anova(m2)$`Sum Sq`;names(ss)<-rownames(anova(m2))
100*ss / sum(ss) 


##
# check distrib of distances between averages relative to confidence intervals
sres = res %>% group_by(parid,scenario) %>% summarize(avgRelativeAccessibility = mean(relativeAccessibility),ci = sd(relativeAccessibility)*1.96/sqrt(n()))
sres$id = paste0(sres$parid,"-",sres$scenario)

#relci = c()
#for(i in 1:(nrow(sres)-1)){
#  show(i)
#  for(j in (i+1):nrow(sres)){
#    relci = append(relci,(sres$ci[i]+sres$ci[j])/abs(sres$avgRelativeAccessibility[i]-sres$avgRelativeAccessibility[j]))
#  }
#}

relci=c((matrix(rep(sres$ci,nrow(sres)),nrow=nrow(sres),byrow = T)+matrix(rep(sres$ci,nrow(sres)),nrow=nrow(sres),byrow = F))/(abs(matrix(rep(sres$avgRelativeAccessibility,nrow(sres)),nrow=nrow(sres),byrow = T)-matrix(rep(sres$avgRelativeAccessibility,nrow(sres)),nrow=nrow(sres),byrow = F))))
#hist(relci,breaks=1000)
#quantile(relci,0.975)
#97.5% 
#0.9686911 




####
## Correlation indicators

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(corAccessDev),ci = sd(corAccessDev)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=corAccessDev,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(rho*"["*X*","*Delta*"P]"))+stdtheme
ggsave(filename = paste0(resdir,'distrib_rhoAccessDev.png'),width=15,height=10,units='cm')

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(corAccessEmployments),ci = sd(corAccessEmployments)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=corAccessEmployments,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(rho*"["*X*","*Delta*"P]"))+stdtheme
ggsave(filename = paste0(resdir,'distrib_rhoAccessEmployments.png'),width=15,height=10,units='cm')

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(corAccessTimeUnweighted),ci = sd(corAccessTimeUnweighted)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=corAccessTimeUnweighted,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(rho*"["*X*","*Delta*"P]"))+stdtheme
ggsave(filename = paste0(resdir,'distrib_rhoAccessTimeUnweighted.png'),width=15,height=10,units='cm')

sres = res %>% group_by(parid,scenario) %>% summarize(avg = mean(corAccessTimeWeighted),ci = sd(corAccessTimeWeighted)*1.96/sqrt(n()))
g = ggplot(res[res$parid%in%parids,],aes(x=corAccessTimeWeighted,group=paste0(parid,"-",scenario),colour=paste0(parid,"-",scenario)))
g+geom_density(alpha=0.4)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg,colour=paste0(parid,"-",scenario)))+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg-ci,colour=paste0(parid,"-",scenario)),linetype=2)+geom_vline(data=sres[sres$parid%in%parids,],aes(xintercept=avg+ci,colour=paste0(parid,"-",scenario)),linetype=2)+
  scale_color_discrete(name="id-scenario")+xlab(expression(rho*"["*X*","*Delta*"P]"))+stdtheme
ggsave(filename = paste0(resdir,'distrib_rhoAccessTimeUnweighted.png'),width=15,height=10,units='cm')











