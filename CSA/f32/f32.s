    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
i:               .word  0x0
tmp:             .word  0x0
n_orig:          .word  0x0
sqrt_n:          .word  0x0
newton_its:      .word  8

    .text
    .org 0x90
_start:

    @p input_addr a! @       \ n:[]

    is_prime

    @p output_addr a! !
    halt

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    \ T = divisible
    \ S = divider
    \ returns:
    \ T = main
    \ S = remainder
divide:
    @p tmp b!                \ B <- tmp*
    a!                       \ A <- divisible
    !b                       \ n:[]   tmp <- divider
    lit 0 lit 0

    lit 31 >r
divide_loop:
    +/
    next divide_loop
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    \ T = divisible
    \ S = divider
    \ returns:
    \ T = 0 if divider else T = 1
is_divider:
    divide
    drop
    if no_remainder
    lit 1
    ;

no_remainder:
    lit 0
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    \ T = n
    \ returns:
    \ T = floor(sqrt(n)) + 1
sqrt_ceil:
    dup
    inv lit 1 +
    lit 10000 +
    -if newton_method        \ is n <= 10000
    lit 16 !p newton_its

newton_method:
    dup                      \ x = n
    !p n_orig                \ x:[];  n_orig <- n


    @p newton_its >r
iteration:
    dup @p n_orig            \ n:x:x:[]
    divide
    over drop                \ main:x:[]
    +
    2/
    next iteration
    lit 1 +
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    \ T = n
    \ returns:
    \ T = 1 if prime else T = 0
is_prime:
    dup
    lit -1 +                 \ -1 + n:n:[]
    -if more_or_equal_1      \ is n < 1
    error ;

more_or_equal_1:
    dup
    lit 1
    xor
    if return_0              \ is n = 1

    dup
    lit 2
    xor
    if return_1              \ is n = 2

    dup lit 1 and            \ n & 1
    if return_0

prepare:
    dup
    sqrt_ceil
    !p sqrt_n                \ sqrt_n <- sqrt(n)

    lit 3 !p i               \ i <- 3

    \ while i < sqrt(n)
while:
    dup
    @p i over                \ n:i:n:[]

    is_divider               \ result:n:[]

    if return_0              \ n:[]

continue:
    @p i lit 2 +
    !p i                     \ i = i + 2

    @p sqrt_n
    @p i
    inv lit 1 +
    +                        \ sqrt_n - i:n:[]

    -if while                \ is i <= sqrt_n
    return_1


return_1:
    drop
    lit 1                    \ 1:[]
    ;

return_0:
    drop
    lit 0
    ;

error:
    drop
    lit -1
    ;

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
