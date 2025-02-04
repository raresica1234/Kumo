export class FileUtils {
  private static supportedTypes = ['B', 'KiB', 'MiB', 'GiB', 'TiB'];
  public static displaySize(size: number): string {
    let unitIndex = 0;
    while (size > 1150 && unitIndex < this.supportedTypes.length - 1) {
      size = size / 1024;
      unitIndex++;
    }
    return size.toFixed(1) + ' ' + this.supportedTypes[unitIndex];
  }
}
