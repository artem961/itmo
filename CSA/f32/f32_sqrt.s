    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
i:               .word  0x0
tmp:             .word  0x0

    .text
    .org 0x90
_start:

    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @p input_addr a! @       \ n:[]

    sqrt_ceil

    @p output_addr a! !
    halt
    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
sqrt_ceil: