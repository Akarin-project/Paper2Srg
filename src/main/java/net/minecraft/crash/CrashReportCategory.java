package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class CrashReportCategory {

    private final CrashReport field_85078_a;
    private final String field_85076_b;
    private final List<CrashReportCategory.Entry> field_85077_c = Lists.newArrayList();
    private StackTraceElement[] field_85075_d = new StackTraceElement[0];

    public CrashReportCategory(CrashReport crashreport, String s) {
        this.field_85078_a = crashreport;
        this.field_85076_b = s;
    }

    public static String func_180522_a(BlockPos blockposition) {
        return func_184876_a(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
    }

    public static String func_184876_a(int i, int j, int k) {
        StringBuilder stringbuilder = new StringBuilder();

        try {
            stringbuilder.append(String.format("World: (%d,%d,%d)", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k)}));
        } catch (Throwable throwable) {
            stringbuilder.append("(Error finding world loc)");
        }

        stringbuilder.append(", ");

        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;

        try {
            l = i >> 4;
            i1 = k >> 4;
            j1 = i & 15;
            k1 = j >> 4;
            l1 = k & 15;
            i2 = l << 4;
            j2 = i1 << 4;
            k2 = (l + 1 << 4) - 1;
            l2 = (i1 + 1 << 4) - 1;
            stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2)}));
        } catch (Throwable throwable1) {
            stringbuilder.append("(Error finding chunk loc)");
        }

        stringbuilder.append(", ");

        try {
            l = i >> 9;
            i1 = k >> 9;
            j1 = l << 5;
            k1 = i1 << 5;
            l1 = (l + 1 << 5) - 1;
            i2 = (i1 + 1 << 5) - 1;
            j2 = l << 9;
            k2 = i1 << 9;
            l2 = (l + 1 << 9) - 1;
            int i3 = (i1 + 1 << 9) - 1;

            stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2), Integer.valueOf(i3)}));
        } catch (Throwable throwable2) {
            stringbuilder.append("(Error finding world loc)");
        }

        return stringbuilder.toString();
    }

    public void func_189529_a(String s, ICrashReportDetail<String> crashreportcallable) {
        try {
            this.func_71507_a(s, crashreportcallable.call());
        } catch (Throwable throwable) {
            this.func_71499_a(s, throwable);
        }

    }

    public void func_71507_a(String s, Object object) {
        this.field_85077_c.add(new CrashReportCategory.Entry(s, object));
    }

    public void func_71499_a(String s, Throwable throwable) {
        this.func_71507_a(s, (Object) throwable);
    }

    public int func_85073_a(int i) {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();

        if (astacktraceelement.length <= 0) {
            return 0;
        } else {
            this.field_85075_d = new StackTraceElement[astacktraceelement.length - 3 - i];
            System.arraycopy(astacktraceelement, 3 + i, this.field_85075_d, 0, this.field_85075_d.length);
            return this.field_85075_d.length;
        }
    }

    public boolean func_85069_a(StackTraceElement stacktraceelement, StackTraceElement stacktraceelement1) {
        if (this.field_85075_d.length != 0 && stacktraceelement != null) {
            StackTraceElement stacktraceelement2 = this.field_85075_d[0];

            if (stacktraceelement2.isNativeMethod() == stacktraceelement.isNativeMethod() && stacktraceelement2.getClassName().equals(stacktraceelement.getClassName()) && stacktraceelement2.getFileName().equals(stacktraceelement.getFileName()) && stacktraceelement2.getMethodName().equals(stacktraceelement.getMethodName())) {
                if (stacktraceelement1 != null != this.field_85075_d.length > 1) {
                    return false;
                } else if (stacktraceelement1 != null && !this.field_85075_d[1].equals(stacktraceelement1)) {
                    return false;
                } else {
                    this.field_85075_d[0] = stacktraceelement;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void func_85070_b(int i) {
        StackTraceElement[] astacktraceelement = new StackTraceElement[this.field_85075_d.length - i];

        System.arraycopy(this.field_85075_d, 0, astacktraceelement, 0, astacktraceelement.length);
        this.field_85075_d = astacktraceelement;
    }

    public void func_85072_a(StringBuilder stringbuilder) {
        stringbuilder.append("-- ").append(this.field_85076_b).append(" --\n");
        stringbuilder.append("Details:");
        Iterator iterator = this.field_85077_c.iterator();

        while (iterator.hasNext()) {
            CrashReportCategory.Entry crashreportsystemdetails_crashreportdetail = (CrashReportCategory.Entry) iterator.next();

            stringbuilder.append("\n\t");
            stringbuilder.append(crashreportsystemdetails_crashreportdetail.func_85089_a());
            stringbuilder.append(": ");
            stringbuilder.append(crashreportsystemdetails_crashreportdetail.func_85090_b());
        }

        if (this.field_85075_d != null && this.field_85075_d.length > 0) {
            stringbuilder.append("\nStacktrace:");
            StackTraceElement[] astacktraceelement = this.field_85075_d;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement[j];

                stringbuilder.append("\n\tat ");
                stringbuilder.append(stacktraceelement);
            }
        }

    }

    public StackTraceElement[] func_147152_a() {
        return this.field_85075_d;
    }

    public static void func_180523_a(CrashReportCategory crashreportsystemdetails, final BlockPos blockposition, final Block block, final int i) {
        final int j = Block.func_149682_b(block);

        crashreportsystemdetails.func_189529_a("Block type", new ICrashReportDetail() {
            public String a() throws Exception {
                try {
                    return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), block.func_149739_a(), block.getClass().getCanonicalName()});
                } catch (Throwable throwable) {
                    return "ID #" + i;
                }
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Block data value", new ICrashReportDetail() {
            public String a() throws Exception {
                if (i < 0) {
                    return "Unknown? (Got " + i + ")";
                } else {
                    String s = String.format("%4s", new Object[] { Integer.toBinaryString(i)}).replace(" ", "0");

                    return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s});
                }
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Block location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.func_180522_a(blockposition);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public static void func_175750_a(CrashReportCategory crashreportsystemdetails, final BlockPos blockposition, final IBlockState iblockdata) {
        crashreportsystemdetails.func_189529_a("Block", new ICrashReportDetail() {
            public String a() throws Exception {
                return iblockdata.toString();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Block location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.func_180522_a(blockposition);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    static class Entry {

        private final String field_85092_a;
        private final String field_85091_b;

        public Entry(String s, Object object) {
            this.field_85092_a = s;
            if (object == null) {
                this.field_85091_b = "~~NULL~~";
            } else if (object instanceof Throwable) {
                Throwable throwable = (Throwable) object;

                this.field_85091_b = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            } else {
                this.field_85091_b = object.toString();
            }

        }

        public String func_85089_a() {
            return this.field_85092_a;
        }

        public String func_85090_b() {
            return this.field_85091_b;
        }
    }
}
