package es.dam.psp.ut1.practicas;

public class BuscarProcesos {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: BuscarProcesos <cadena>");
            return;
        }
        String buscada = args[0];

        ProcessHandle yo = ProcessHandle.current();
        System.out.println("Soy el PID " + yo.pid() + ", mi padre es "
                + yo.parent().map(ProcessHandle::pid).orElse(-1L));
        System.out.printf("%-8s %-8s %-12s %-26s %-12s %s%n",
                "PID", "PPID", "USUARIO", "INICIO", "CPU", "COMANDO");

        ProcessHandle.allProcesses()
                .filter(p -> p.info().command().isPresent())
                .filter(p -> p.info().command().get().contains(buscada))
                .forEach(p -> {
                    ProcessHandle.Info info = p.info();
                    System.out.printf("%-8d %-8s %-12s %-26s %-12s %s%n",
                            p.pid(),
                            p.parent().map(padre -> String.valueOf(padre.pid())).orElse("-"),
                            ultimos(info.user().orElse("?"), 12),
                            info.startInstant().map(String::valueOf).orElse("?"),
                            info.totalCpuDuration().map(String::valueOf).orElse("?"),
                            ultimos(info.command().get(), 60));
                });
    }

    static String ultimos(String s, int n) {
        return s.length() <= n ? s : s.substring(s.length() - n);
    }
}