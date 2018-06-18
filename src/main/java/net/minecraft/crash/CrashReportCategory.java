package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class CrashReportCategory {

    private final CrashReport crashReport;
    private final String name;
    private final List<CrashReportCategory.Entry> children = Lists.newArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];

    public CrashReportCategory(CrashReport crashreport, String s) {
        this.crashReport = crashreport;
        this.name = s;
    }

    public static String getCoordinateInfo(BlockPos blockposition) {
        return getCoordinateInfo(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    public static String getCoordinateInfo(int i, int j, int k) {
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

    public void addDetail(String s, ICrashReportDetail<String> crashreportcallable) {
        try {
            this.addCrashSection(s, crashreportcallable.call());
        } catch (Throwable throwable) {
            this.addCrashSectionThrowable(s, throwable);
        }

    }

    public void addCrashSection(String s, Object object) {
        this.children.add(new CrashReportCategory.Entry(s, object));
    }

    public void addCrashSectionThrowable(String s, Throwable throwable) {
        this.addCrashSection(s, (Object) throwable);
    }

    public int getPrunedStackTrace(int i) {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();

        if (astacktraceelement.length <= 0) {
            return 0;
        } else {
            this.stackTrace = new StackTraceElement[astacktraceelement.length - 3 - i];
            System.arraycopy(astacktraceelement, 3 + i, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement stacktraceelement, StackTraceElement stacktraceelement1) {
        if (this.stackTrace.length != 0 && stacktraceelement != null) {
            StackTraceElement stacktraceelement2 = this.stackTrace[0];

            if (stacktraceelement2.isNativeMethod() == stacktraceelement.isNativeMethod() && stacktraceelement2.getClassName().equals(stacktraceelement.getClassName()) && stacktraceelement2.getFileName().equals(stacktraceelement.getFileName()) && stacktraceelement2.getMethodName().equals(stacktraceelement.getMethodName())) {
                if (stacktraceelement1 != null != this.stackTrace.length > 1) {
                    return false;
                } else if (stacktraceelement1 != null && !this.stackTrace[1].equals(stacktraceelement1)) {
                    return false;
                } else {
                    this.stackTrace[0] = stacktraceelement;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void trimStackTraceEntriesFromBottom(int i) {
        StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - i];

        System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
        this.stackTrace = astacktraceelement;
    }

    public void appendToStringBuilder(StringBuilder stringbuilder) {
        stringbuilder.append("-- ").append(this.name).append(" --\n");
        stringbuilder.append("Details:");
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext()) {
            CrashReportCategory.Entry crashreportsystemdetails_crashreportdetail = (CrashReportCategory.Entry) iterator.next();

            stringbuilder.append("\n\t");
            stringbuilder.append(crashreportsystemdetails_crashreportdetail.getKey());
            stringbuilder.append(": ");
            stringbuilder.append(crashreportsystemdetails_crashreportdetail.getValue());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0) {
            stringbuilder.append("\nStacktrace:");
            StackTraceElement[] astacktraceelement = this.stackTrace;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j) {
                StackTraceElement stacktraceelement = astacktraceelement[j];

                stringbuilder.append("\n\tat ");
                stringbuilder.append(stacktraceelement);
            }
        }

    }

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    public static void addBlockInfo(CrashReportCategory crashreportsystemdetails, final BlockPos blockposition, final Block block, final int i) {
        final int j = Block.getIdFromBlock(block);

        crashreportsystemdetails.addDetail("Block type", new ICrashReportDetail() {
            public String a() throws Exception {
                try {
                    return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), block.getUnlocalizedName(), block.getClass().getCanonicalName()});
                } catch (Throwable throwable) {
                    return "ID #" + i;
                }
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Block data value", new ICrashReportDetail() {
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
        crashreportsystemdetails.addDetail("Block location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.getCoordinateInfo(blockposition);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public static void addBlockInfo(CrashReportCategory crashreportsystemdetails, final BlockPos blockposition, final IBlockState iblockdata) {
        crashreportsystemdetails.addDetail("Block", new ICrashReportDetail() {
            public String a() throws Exception {
                return iblockdata.toString();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Block location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.getCoordinateInfo(blockposition);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    static class Entry {

        private final String key;
        private final String value;

        public Entry(String s, Object object) {
            this.key = s;
            if (object == null) {
                this.value = "~~NULL~~";
            } else if (object instanceof Throwable) {
                Throwable throwable = (Throwable) object;

                this.value = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            } else {
                this.value = object.toString();
            }

        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
