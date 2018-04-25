
library(segmented)

#'
#'
#'
timecorrs <- function(x,y,n=1,b=1,windows=NULL){
  timesteps = length(x)/(n*b)
  allrho=c();rhominus=c();rhoplus=c();alltws=c();allt0s=c()
  tws=1:timesteps
  if(!is.null(windows)){tws = windows}
  for(tw in tws){
    # test on all possible windows of length tw
    for(t0 in 1:(timesteps-tw+1)){
      inds = ((t0-1)*n+1):((t0+tw-1)*n)
      allinds = c();for(bb in 1:b){allinds = append(allinds,inds + (bb - 1)*(n*timesteps))}
      rho = cor.test(x[allinds],y[allinds])
      show(paste0("Tw = ",tw," ; t0 = ",t0," ; rho = ",rho$estimate," [",rho$conf.int[1],",",rho$conf.int[2],"]"))
      allrho=append(allrho,rho$estimate);rhominus=append(rhominus,rho$conf.int[1]);rhoplus=append(rhoplus,rho$conf.int[2]);alltws=append(alltws,tw);allt0s=append(allt0s,t0)
    }
  }
  return(list(allrho=allrho,rhominus=rhominus,rhoplus=rhoplus,alltws=alltws,allt0s=allt0s))
}


#'
#' 
corrtrends <- function(x,y,n=1,b=1,K=10){
  correlations = timecorrs(x,y,n,b,windows = c(1))
  res = segmented(lm(data=data.frame(rho=correlations$allrho,t=correlations$allt0s),rho~t))
  return(res)
}



#test
#res=corrtrends(rnorm(10000),rnorm(10000),n=1000,b=1)
#x=1:100
#y=c(2*(1:50),200-2*(51:100))+rnorm(100,sd = 10)
#plot(x,y,type='l')
#segmented(lm(data=data.frame(x,y),y~x))

