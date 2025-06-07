import codecs
import hashlib
import re
import sys
import unicodedata
import argparse
from typing import Dict, List, Union



NAME = "BE"
VERSION = "1.0.7"


class Color:
    RED = '\033[91m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    BLUE = '\033[94m'
    MAGENTA = '\033[95m'
    CYAN = '\033[96m'
    RESET = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def refunc(string: str, step: int) -> str:
    """Format string by inserting spaces at regular intervals."""
    return ' '.join(string[i:i + step] for i in range(0, len(string), step))


def buc(length: int, alignment: int) -> str:
    """Generate padding zeros for alignment."""
    if length == alignment or length == 0 or alignment == 0:
        return ""
    elif length < alignment:
        return "0" * (alignment - length)
    elif length % alignment == 0:
        return ""
    else:
        return "0" * (alignment - (length % alignment))


def test(base: str, string: str) -> bool:
    """Validate if string matches the given base format."""
    match str(base):
        case "0B":  # Binary
            return all(c in '01' for c in string)
        case "0O":  # Octal
            return all(c in '01234567' for c in string)
        case "0D":  # Decimal
            return all(c in '0123456789' for c in string)
        case "0X":  # Hexadecimal
            return all(c in '0123456789ABCDEFabcdef' for c in string)
        case _:
            return False


def sep(s: str, v: int) -> str:
    padded = buc(len(s), v) + s
    return refunc(padded, v)


class Cast:
    def __init__(self, value: Union[str, int, float]):
        self.str = str(value[0])

    def hex(self) -> str:
        """Convert to hexadecimal representation."""
        hex_str = ''.join(f"{ord(c):02X}" for c in self.str)
        return refunc(hex_str, 2)

    def oct(self) -> str:
        """Convert to octal representation."""
        oct_str = ''.join(f"{ord(c):03o}" for c in self.str)
        return refunc(oct_str, 4)

    def bin(self) -> str:
        """Convert to binary representation."""
        bin_str = ''.join(f"{ord(c):08b}" for c in self.str)
        return refunc(bin_str, 8)

    def unicode(self) -> Dict[str, str]:
        """Get Unicode code points for each character."""
        result = {}
        for c in self.str:
            code = ord(c)
            result[c] = f"U+{code:04X}" if code <= 0xFFFF else f"U+{code:08X}"
        return result

    def hash(self) -> int:
        """Get built-in hash value."""
        return hash(self.str.encode())

    def hash256(self) -> str:
        """Compute SHA-256 hash."""
        return hashlib.sha256(self.str.encode()).hexdigest()

    def hash512(self) -> str:
        """Compute SHA-512 hash."""
        return hashlib.sha512(self.str.encode()).hexdigest()

    def numeric(self, base: int = 10) -> str:
        """Convert number between bases (2, 8, 10, 16)."""
        if base not in {2, 8, 10, 16}:
            return f"{Color.RED}Error: Unsupported base {base}. Must be 2, 8, 10 or 16.{Color.RESET}"

        try:
            # Clean and identify input format
            cleaned = self.str.strip().upper()
            prefix = cleaned[:2]
            value_str = (cleaned[2:] if prefix in {"0B", "0O", "0X", "0D"} else cleaned).replace(".", "")


            # Determine input base
            if value_str.isdigit() and prefix in {"0B", "0O", "0D"}:
                if prefix == "0B" and test("0B", value_str):
                    input_base = 2
                elif prefix == "0O" and test("0O", value_str):
                    input_base = 8
                else:
                    input_base = 10
            elif test("0B", value_str):
                input_base = 2
            elif test("0O", value_str):
                input_base = 8
            elif test("0D", value_str):
                input_base = 10
            elif test("0X", value_str):
                input_base = 16
            else:
                return f"{Color.RED}Error: Invalid numeric format '{self.str}'.{Color.RESET}"

            # Convert to integer and format output
            if input_base != base:
                num = int(value_str, input_base) if prefix else int(cleaned, input_base)
            else:
                return sep(value_str, 8 if input_base == 2 else 4 if input_base == 8 else 2 if input_base == 16 else 0)

            match int(base):
                case 2:  # Binary
                    bin_str = bin(num)[2:]
                    return sep(bin_str, 8)
                case 8:  # Octal
                    oct_str = oct(num)[2:]
                    return sep(oct_str, 4)
                case 16:  # Hexadecimal
                    hex_str = hex(num)[2:].upper()
                    return sep(hex_str, 2)
                case _:  # Decimal
                    return str(num)

        except ValueError as e:
            return f"{Color.RED}Conversion error: {e}{Color.RESET}"

    def decode(self) -> str:
        """Decode escaped Unicode characters."""
        try:
            return codecs.decode(self.str, "unicode_escape")
        except UnicodeDecodeError:
            return f"Unicode symbol of {self.str} is unknown."

    def name(self) -> Union[str, Dict[str, str]]:
        """Get Unicode names for characters."""
        if len(self.str) == 0:
            return f"{Color.YELLOW}Warning: Empty input{Color.RESET}"

        results = {}
        for c in self.str:
            try:
                results[c] = unicodedata.name(c)
            except ValueError:
                results[c] = "UNKNOWN"

        return results if len(results) > 1 else list(results.values())[0]

    def __len__(self) -> int:
        return len(self.str)

    def __repr__(self) -> str:
        return self.str

    def __str__(self) -> str:
        return self.str


def print_help():
    """Display comprehensive help information."""
    help_text = f"""
{Color.BOLD}{NAME} Tool v{VERSION}{Color.RESET}
{Color.UNDERLINE}Convert and analyze text/numeric values{Color.RESET}

{Color.BOLD}Usage:{Color.RESET}
    be [OPTION] [COMMAND] [INPUT]

{Color.BOLD}Options:{Color.RESET}
    -h, --help     Show this help message
    -v, --version  Show version information

{Color.BOLD}Commands:{Color.RESET}
    hex        Convert text to hexadecimal
    oct        Convert text to octal
    bin        Convert text to binary
    hash       Get built-in hash value
    hash256    Compute SHA-256 hash
    hash512    Compute SHA-512 hash
    code       Show Unicode code points
    echo       Output original text
    find       Decode escaped characters
    name       Get Unicode character name(s)
    n[base]    Convert numbers between bases (n2, n8, n10, n16)

{Color.BOLD}Examples:{Color.RESET}
    be hex "Hello"        # Convert text to hex
    be n16 255            # Convert 255 to hex
    be name "A"           # Show Unicode name for 'A'
    be find "\\u03A9"      # Decode Unicode escape
"""
    print(help_text)


def print_version():
    print(f"{Color.BOLD}{NAME} version {Color.GREEN}{VERSION}{Color.RESET}")


def print_default():
    print(f"{NAME}{Color.RESET}: fatal: no input args specified\n"
          f"Type {NAME} -h for help.")


def main():
    parser = argparse.ArgumentParser(prog="be", description=NAME, add_help=False)
    parser.add_argument('command', nargs='?', default=None)
    parser.add_argument('input', nargs='*', default=None)
    parser.add_argument( '-v', '--version', action='store_true')
    parser.add_argument('-h', '-?', '--help', action='store_true')

    args = parser.parse_args()

    if not args.command and len(sys.argv) == 1:
        print_default()
        return

    if args.help:
        print_help()
        return

    if args.version:
        print_version()
        return

    if not args.input:
        print(f"{Color.RED}Error: Missing input value{Color.RESET}")
        print(f"Usage: be [command] [input]")
        return

    t = Cast(args.input)
    command = args.command.lower()

    try:
        match str(command):
            case "hex":
                print(t.hex())
            case "oct":
                print(t.oct())
            case "bin":
                print(t.bin())
            case "hash":
                print(t.hash())
            case "hash256":
                print(t.hash256())
            case "hash512":
                print(t.hash512())
            case "code":
                for char, code in t.unicode().items():
                    print(f"{char}: {code}")
            case "echo":
                print(t)
            case "find":
                print(t.decode())
            case "name":
                result = t.name()
                if isinstance(result, dict):
                    for char, name in result.items():
                        print(f"{char}: {name}")
                else:
                    print(result)
            case "num" | "n":
                print(t.numeric(10))
            case _ if command.startswith('n') and command[1:].isdigit():
                base = int(command[1:])
                print(t.numeric(base))
            case _:
                print(f"{Color.RED}Error: Unknown command '{command}'{Color.RESET}")
                print_help()
    except Exception as e:
        print(f"{Color.RED}Error: {e}{Color.RESET}")


if __name__ == "__main__":
    main()