

#'
#'
#'
timecorrs <- function(x,y,n=1,b=1){
  timesteps = length(x)/(n*b)
  #for(tw in 1:timesteps){
    rho = cor.test(x,y)
  #}
  return(c(rho$estimate,rho$conf.int[1],rho$conf.int[2]))
}

