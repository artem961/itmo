    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
i:               .word  0
len_bytes:       .word  4
tmp:             .word  0x00
const_1:         .word  1
const_FF:        .word  0x000000FF
shift_bits:      .word  8
ans:             .word  0x00

    .text

_start:
    load         len_bytes
    store        i                           ; i <- len_bytes

    load         input_addr
    load_acc
    store        tmp                         ; tmp <- input

while:
    beqz         end                         ; while (i != 0) {

    load         tmp
    and          const_FF                    ; acc & 0x000000FF
    add          ans
    store        ans                         ; ans <- ans + acc

    load         i
    sub          const_1
    store        i                           ;     i <- i - const_1


    beqz         end                         ; if (i == 0) break

    load         ans                         ;
    shiftl       shift_bits
    store        ans                         ; ans <- ans << 8

    load         tmp
    shiftr       shift_bits
    store        tmp                         ; tmp <- tmp >> 8

    load         i
    jmp          while                       ; }

end:
    load         ans
    store_ind    output_addr                 ; *output_addr <- ans
    halt
