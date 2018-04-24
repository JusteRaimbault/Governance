

#'
#'
#'
timecorrs <- function(x,y,n=1,b=1){
  timesteps = length(x)/(n*b)
  allrho=c();rhominus=c();rhoplus=c();alltws=c();allt0s=c()
  for(tw in 1:timesteps){
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

