
library(ggplot2)

source(paste0(Sys.getenv('CS_HOME'),'/Organisation/Models/Utils/R/plots.R'))

setwd(paste0(Sys.getenv('CS_HOME'),'/Governance/Models/Lutecia/analysis'))


# study of the behavior of pi as a function of J and accessibility diff in the discrete choice game case

pi <- function(J,deltaAcc_i,deltaAcc_other,betaDC){
  res <- uniroot(function(x){
    x - 1 / (1 + exp(- betaDC*(deltaAcc_i/(1 + exp(- betaDC*(x*deltaAcc_other - J))) - J)))
  },interval=c(0,1))
  return(res$root)
}

res=data.frame()
for(betaDC in c(100,200,400)#c(1,10,100,200,400)
    ){
  for(deltaAcc_i in c(0.001,0.005,0.01,0.02,0.03,0.04,0.05) # c(0.001,0.005,0.01,0.05)
      ){
    for(deltaAcc_other in c(0.001,0.01,0.05)){
      for(J in seq(0.0001,0.005,by=0.0001)){
        res=rbind(res,c(betaDC=betaDC,deltaAcc_i=deltaAcc_i,deltaAcc_other=deltaAcc_other,J=J,
                        pi=pi(J,deltaAcc_i,deltaAcc_other,betaDC)
                        ))
      }
    }
  }
}
names(res)<-c("betaDC","deltaAcc_i","deltaAcc_other","J","pi")


g=ggplot(res,aes(x=J,y=pi,color=deltaAcc_i,group=deltaAcc_i))
g+geom_line()+facet_grid(betaDC~deltaAcc_other,scales="free")+
  scale_x_continuous(breaks=seq(0.0,0.004,by=0.001))+
  scale_color_continuous(name=expression(Delta[i]))+ylab(expression(p[i]))+stdtheme
ggsave(filename = paste0(Sys.getenv('CS_HOME'),'/Governance/Results/Lutecia/Theoretical/discretechoice-game-probas.png'),width=33,height=30,units='cm')





