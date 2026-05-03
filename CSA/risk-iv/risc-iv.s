    .data
.org             0x00
buf:             .byte  '________________________________'
input_addr:      .word  0x80
output_addr:     .word  0x84
len_limit:       .word  31
stop_symbol:     .byte  10
overflow_value:  .word  0xCCCCCCCC

    .text
    .org     0x90
_start:
    addi     sp, zero, 500                   ; init sp

    lui      t0, %hi(input_addr)
    addi     t0, t0, %lo(input_addr)
    lw       t0, 0(t0)                       ; t0 <- *input_addr

    lui      t1, %hi(output_addr)
    addi     t1, t1, %lo(output_addr)
    lw       t1, 0(t1)                       ; t1 <- *output_addr

    lui      t2, %hi(buf)
    addi     t2, t2, %lo(buf)                ; t2 = buf*

    lui      t3, %hi(len_limit)
    addi     t3, t3, %lo(len_limit)
    lw       t3, 0(t3)                       ; t3 <- *len_limit

    lui      t5, %hi(stop_symbol)
    addi     t5, t5, %lo(stop_symbol)
    lb       t5, 0(t5)                       ; t5 = '\n'

while:
    lb       t4, 0(t0)                       ; tmp = *input
    beq      t4, t5, finish                  ; if (t4 == '\n') -> finish
    beqz     t3, overflow                    ; limit == 0 -> overflow

    mv       a0, t4                          ; a0 - func argument
    jal      ra, to_upper
    mv       t4, a0

    sb       t4, 0(t2)                       ; *buf = t4

    addi     t2, t2, 1                       ; buf*++
    addi     t3, t3, -1                      ; limit--
    j        while

overflow:
    lui      t4, %hi(overflow_value)
    addi     t4, t4, %lo(overflow_value)
    lw       t4, 0(t4)
    sw       t4, 0(t1)
    j        end

finish:
    sb       zero, 0(t2)                     ; store '\0' to the end

    lui      a0, %hi(buf)
    addi     a0, a0, %lo(buf)
    mv       a1, t1
    jal      ra, print_string                ; a0 = buf*  a1 = *output_addr

end:
    halt


    ; Функция: to_upper
    ; Аргументы: a0 - символ
    ; Возврат: a0 - символ
to_upper:
    addi     sp, sp, -4
    sw       t6, 0(sp)                       ; save t6

    addi     t6, zero, 96
    ble      a0, t6, to_upper_end
    addi     t6, zero, 122
    bgt      a0, t6, to_upper_end
    addi     a0, a0, -32

to_upper_end:
    lw       t6, 0(sp)
    addi     sp, sp, 4                       ; restore t6
    jr       ra


    ; Функция: print_string
    ; Аргументы: a0 - указатель на строку, a1 - адрес вывода
print_string:
    addi     sp, sp, -12
    sw       ra, 8(sp)
    sw       t4, 4(sp)
    sw       t6, 0(sp)                       ; save t4, t6, ra

    mv       t4, a0
    mv       t6, a1

print_string_loop:
    lb       a0, 0(t4)
    beqz     a0, print_string_end

    mv       a1, t6
    jal      ra, print_char                  ; call print_char

    addi     t4, t4, 1                       ; buf*++
    j        print_string_loop

print_string_end:

    lw       ra, 8(sp)
    lw       t4, 4(sp)
    lw       t6, 0(sp)
    addi     sp, sp, 12                      ; restore t4, t6, ra
    jr       ra


    ; Функция: print_char
    ; Аргументы: a0 - символ, a1 - адрес вывода
print_char:
    sb       a0, 0(a1)
    jr       ra
