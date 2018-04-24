

#'
#'
#'
timecorrs <- function(x,y,n=1,b=1){
  timesteps = length(x)/(n*b)
  #for(tw in 1:timesteps){
    # test on all possible windows of length tw
  #  for(t0 in 1:(timesteps-tw+1)){
  #    steps = t0:(t0+tw-1)
      rho = cor.test(x,y)
  #  }
  #}
  return(c(rho$estimate,rho$conf.int[1],rho$conf.int[2]))
}

