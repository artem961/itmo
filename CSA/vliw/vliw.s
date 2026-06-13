    .data
.org             0x00
prime:           .word  0x01000193
basis:           .word  0x811C9DC5
in_addr:         .word  0x80
out_addr:        .word  0x84

    .text
    .org 0x100
_start:
    lui a5, %hi(prime)        / lui a6, %hi(basis)         / nop          / nop
    addi a5, a5, %lo(prime)   / addi a6, a6, %lo(basis)    / nop          / nop

    lui a0, %hi(in_addr)      / lui a1, %hi(out_addr)      / lw t2, 0(a5) / nop
    addi a0, a0, %lo(in_addr) / addi a1, a1, %lo(out_addr) / lw t1, 0(a6) / nop

    nop                       / nop                        / lw a0, 0(a0) / nop
    nop                       / nop                        / lw a1, 0(a1) / nop
    nop                       / nop                        / lw t3, 0(a0) / nop

loop:
    mul t4, t1, t2            / nop                        / nop          / beqz t3, end
    xor t1, t4, t3            / nop                        / lw t3, 0(a0) / j loop

end:
    nop                       / nop                        / sw t1, 0(a1) / nop
    nop                       / nop                        / nop          / halt
