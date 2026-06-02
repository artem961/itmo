    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
buff_addr:       .word  0x90
ovf:             .word  0xCCCCCCCC

    .text
    .org     0x200

_start:
    movea.l  1024, A7                        ; init SP
    clr.l    D1
    clr.l    D3                              ; repit counter
    clr.l    D4                              ; input char counter
    clr.l    D5                              ; buff size counter

    movea.l  input_addr, A0
    movea.l  (A0), A0                        ; A0 = in*

    movea.l  output_addr, A1
    movea.l  (A1), A1                        ; A1 = out*

    movea.l  buff_addr, A2
    movea.l  (A2), A2                        ; A2 = buf*

while:
    cmp.l    0, D3                           ; check if first char needs to be read
    bne      skip_init
    move.l   (A0), D2
    add.l    1, D4
    cmp.l    10, D2
    beq      flush
    move.l   1, D3
skip_init:
    cmp.l    64, D4
    bge      error

    move.l   (A0), D1
    add.l    1, D4

    cmp.l    10, D1
    beq      terminate

    cmp.l    D2, D1                          ; compare matching
    beq      match

diff:
    cmp.l    62, D5
    bge      error

    jsr      write_pair

    move.l   D1, D2
    move.l   1, D3
    jmp      while

match:
    cmp.l    9, D3
    beq      diff
    add.l    1, D3
    jmp      while

terminate:
    cmp.l    62, D5
    bge      error

    jsr      write_pair

    jmp      flush

flush:
    movea.l  buff_addr, A2
    movea.l  (A2), A2
    cmp.l    0, D5
    beq      exit

while2:
    move.l   (A2)+, (A1)
    sub.l    1, D5
    bne      while2
    jmp      exit

error:
    movea.l  ovf, A3
    move.l   (A3), D0
    move.l   D0, (A1)

exit:
    halt


    ; Subroutine
    ; Converts run count to char and stores it
write_pair:
    link     A6, 0

    move.l   D3, D0
    add.l    48, D0                          ; Convert digit to char
    jsr      store_val

    move.l   D2, D0
    jsr      store_val

    unlk     A6
    rts

    ; Subroutine
    ; Stores value from D0 to buff and increments size
store_val:
    link     A6, 0
    move.l   D0, (A2)+
    add.l    1, D5
    unlk     A6
    rts
